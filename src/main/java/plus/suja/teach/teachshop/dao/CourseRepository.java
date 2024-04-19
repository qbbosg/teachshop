package plus.suja.teach.teachshop.dao;

import org.springframework.data.repository.CrudRepository;
import plus.suja.teach.teachshop.entity.Course;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    @Override
    List<Course> findAll();
}
