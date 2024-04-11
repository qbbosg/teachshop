package plus.suja.teach.teachshop.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import plus.suja.teach.teachshop.entity.Member;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, Integer> {
    Member findByUsername(String username);

    @Query("select m from Member m join m.roles r where r.name = :roleName")
    List<Member> findAllByRoleName(String roleName);
}
