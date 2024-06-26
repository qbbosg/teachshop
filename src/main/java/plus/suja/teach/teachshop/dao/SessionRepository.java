package plus.suja.teach.teachshop.dao;

import org.springframework.data.repository.CrudRepository;
import plus.suja.teach.teachshop.entity.Session;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Integer> {
    Optional<Session> findByCookie(String cookie);

    void deleteByCookie(String cookie);
}
