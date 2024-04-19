package plus.suja.teach.teachshop.dao;

import org.springframework.data.repository.CrudRepository;
import plus.suja.teach.teachshop.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    Optional<Order> findByTradeNo(String tradeNo);

    @Override
    List<Order> findAll();
}
