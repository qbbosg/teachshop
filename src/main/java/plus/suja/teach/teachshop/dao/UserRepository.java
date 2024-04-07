package plus.suja.teach.teachshop.dao;

import org.springframework.data.repository.CrudRepository;
import plus.suja.teach.teachshop.entity.Member;

public interface UserRepository extends CrudRepository<Member, Integer> {
}
