package plus.suja.teach.teachshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import plus.suja.teach.teachshop.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByTradeNo(String tradeNo);

    @Override
    List<Order> findAll();
}
