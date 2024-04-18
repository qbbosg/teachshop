package plus.suja.teach.teachshop.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.OrderRepository;
import plus.suja.teach.teachshop.entity.BizContent;
import plus.suja.teach.teachshop.entity.Course;
import plus.suja.teach.teachshop.entity.Goods;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Order;
import plus.suja.teach.teachshop.entity.Video;
import plus.suja.teach.teachshop.enums.AliPayTradeStatus;
import plus.suja.teach.teachshop.enums.OrderStatus;
import plus.suja.teach.teachshop.exception.HttpException;
import plus.suja.teach.teachshop.util.UserContextUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private CourseService courseService;
    private PayService payService;
    private VideoService videoService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CourseService courseService, PayService payService, VideoService videoService) {
        this.orderRepository = orderRepository;
        this.courseService = courseService;
        this.payService = payService;
        this.videoService = videoService;
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public Order getOrder(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new HttpException(401, "Not found"));
    }

    private Order getOrderByTradeNo(String tradeNo) {
        return orderRepository.findByTradeNo(tradeNo).orElseThrow(() -> new HttpException(404, "Not found"));
    }

    public Order createOrder(Integer courseId, Integer memberId, BigDecimal price, Integer number, HttpServletResponse response) {
        Order order = new Order();
        order.setTradeNo(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.UNPAID);
        order.setCourseId(courseId);
        order.setMemberId(memberId);
        order.setNumber(number);
        order.setPrice(price);
        Order saveOrder;
        try {
            saveOrder = orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        response.setStatus(201);
        return saveOrder;
    }

    public String payOrder(String tradeNo) throws AlipayApiException, JsonProcessingException {
        Order order = getOrderByTradeNo(tradeNo);
        checkPayOrderEnvironment(order);
        AlipayTradePagePayResponse response = payService.alipayTradePagePayRequest(getBizContent(order));
        String pageRedirectionData = response.getBody();

        if (response.isSuccess()) {
            return pageRedirectionData;
        } else {
            System.out.println("调用失败");
            throw new HttpException(404, "Not found");
        }
    }

    private static void checkPayOrderEnvironment(Order order) {
        Member currentUser = UserContextUtil.getCurrentUser();
        if (currentUser == null) {
            throw new HttpException(401, "Unauthorized");
        }
        if (!order.getMemberId().equals(currentUser.getId())) {
            throw new HttpException(403, "Forbid");
        }
    }

    private BizContent getBizContent(Order order) {
        Course course = courseService.getCourse(order.getCourseId());
        List<Video> videoList = videoService.getAllVideosByCourseId(order.getCourseId()).stream().filter(Video::canPlay).collect(Collectors.toList());
        BizContent bizContent = new BizContent();
        Integer timeExpireSeconds = 60 * 3;
        videoList.forEach(video -> {
            Goods goods = new Goods();
            goods.setGoodsId(video.getId().toString());
            goods.setGoodsName(video.getTitle());
            goods.setPrice(video.getPrice().divide(new BigDecimal(100)));
            goods.setQuantity(1);
            bizContent.setGoodsDetails(goods);
        });
        bizContent.setOutTradeNo(order.getTradeNo());
        bizContent.setTotalAmount(order.getPrice().divide(new BigDecimal(100)));
        bizContent.setSubject(course.getTitle());
        bizContent.setTimeExpire(LocalDateTime.now().plusSeconds(timeExpireSeconds).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return bizContent;
    }


    public Order checkPay(String outTradeNo) throws AlipayApiException {
        AlipayTradeQueryResponse response = payService.alipayTradeQueryRequest(outTradeNo);
        if (response.isSuccess()) {
            Order order = getOrderByTradeNo(outTradeNo);
            setOrderStatusAndSaveOrder(order, AliPayTradeStatus.valueOf(response.getTradeStatus()));
            return order;
        } else {
            if ("40004".equals(response.getCode()) && "ACQ.TRADE_NOT_EXIST".equals(response.getSubCode())) {
                Order order = getOrderByTradeNo(outTradeNo);
                setOrderStatusAndSaveOrder(order, AliPayTradeStatus.TRADE_TIMEOUT);
            }
            throw new HttpException(404, "Not found");
        }
    }

    private void setOrderStatusAndSaveOrder(Order order, AliPayTradeStatus tradeStatus) {
        switch (tradeStatus) {
            case WAIT_BUYER_PAY:
                order.setStatus(OrderStatus.UNPAID);
                break;
            case TRADE_CLOSED:
                order.setStatus(OrderStatus.DELETED);
                break;
            case TRADE_TIMEOUT:
                order.setStatus(OrderStatus.TIMEOUT);
                break;
            case TRADE_REFUND:
                order.setStatus(OrderStatus.REFUND);
                break;
            case TRADE_SUCCESS:
            default:
                order.setStatus(OrderStatus.PAID);
                break;
        }
        orderRepository.save(order);
    }

    public Order closeOrder(String orderNo) throws AlipayApiException, JsonProcessingException {
        AlipayTradeCloseResponse response = payService.alipayTradeCloseRequest(orderNo);
        if (response.isSuccess()) {
            Order order = getOrderByTradeNo(orderNo);
            setOrderStatusAndSaveOrder(order, AliPayTradeStatus.TRADE_CLOSED);
            return order;
        } else {
            throw new HttpException(404, "Not found");
        }
    }

    public String refundOrder(String orderNo) throws AlipayApiException, JsonProcessingException {
        Order order = getOrderByTradeNo(orderNo);
        AlipayTradeRefundResponse response = payService.alipayTradeRefundRequest(orderNo, order.getPrice());
        if(response.isSuccess()){
            setOrderStatusAndSaveOrder(order, AliPayTradeStatus.TRADE_REFUND);
            return "退款成功";
        } else {
            return "退款失败";
        }
    }
}
