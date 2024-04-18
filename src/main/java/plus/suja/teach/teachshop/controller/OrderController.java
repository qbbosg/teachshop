package plus.suja.teach.teachshop.controller;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Order;
import plus.suja.teach.teachshop.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<Order> getAllOrder() {
        return orderService.getAllOrder();
    }

    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable Integer id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestParam Integer courseId,
                             @RequestParam Integer memberId,
                             @RequestParam Integer number,
                             @RequestParam BigDecimal price,
                             HttpServletResponse response) {
        return orderService.createOrder(courseId, memberId, price, number, response);
    }

    @GetMapping("/orders/pay")
    public String payOrder(@RequestParam("orderNo") String tradeNo) throws AlipayApiException, JsonProcessingException {
        return orderService.payOrder(tradeNo);
    }

    @GetMapping("/orders/checkPay")
    public Order checkPay(@RequestParam("out_trade_no") String outTradeNo) throws AlipayApiException, JsonProcessingException {
        return orderService.checkPay(outTradeNo);
    }

    @GetMapping("/orders/close")
    public Order closeOrder(@RequestParam("orderNo") String orderNo) throws AlipayApiException, JsonProcessingException {
        return orderService.closeOrder(orderNo);
    }
}
