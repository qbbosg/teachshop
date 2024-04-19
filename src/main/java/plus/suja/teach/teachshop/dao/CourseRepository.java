package plus.suja.teach.teachshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import plus.suja.teach.teachshop.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Override
    List<Course> findAll();
}
