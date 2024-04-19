package plus.suja.teach.teachshop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.OrderRepository;
import plus.suja.teach.teachshop.entity.Order;
import plus.suja.teach.teachshop.entity.PageResponse;
import plus.suja.teach.teachshop.enums.OrderStatus;
import plus.suja.teach.teachshop.exception.HttpException;

import java.util.UUID;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public PageResponse<Order> getAllOrder(Integer pageNum, Integer pageSize) {
        return new PageResponse<Order>().getAllPageResponse(pageNum, pageSize, orderRepository::findAll);
    }

    public Order getOrder(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new HttpException(401, "Not found"));
    }

    public Order getOrderByTradeNo(String tradeNo) {
        return orderRepository.findByTradeNo(tradeNo).orElseThrow(() -> new HttpException(404, "Not found"));
    }

    public Order createOrder(Order order, HttpServletResponse response) {
        order.setTradeNo(UUID.randomUUID().toString());
        Order saveOrder = orderRepository.save(order);
        response.setStatus(201);
        return saveOrder;
    }

    public void modifyOrder(Integer id, Order order) {
        orderRepository.findById(id).ifPresent(hasOrder -> {
            hasOrder.setStatus(order.getStatus());
            orderRepository.save(hasOrder);
        });
    }

    public void deleteOrder(Integer id) {
        orderRepository.findById(id).ifPresent(hasOrder -> {
            hasOrder.setStatus(OrderStatus.DELETED);
            orderRepository.save(hasOrder);
        });
    }
}
