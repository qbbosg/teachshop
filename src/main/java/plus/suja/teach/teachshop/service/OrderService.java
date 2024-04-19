package plus.suja.teach.teachshop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.OrderRepository;
import plus.suja.teach.teachshop.entity.Order;
import plus.suja.teach.teachshop.enums.OrderStatus;
import plus.suja.teach.teachshop.exception.HttpException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public Order getOrder(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new HttpException(401, "Not found"));
    }

    public Order getOrderByTradeNo(String tradeNo) {
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
}
