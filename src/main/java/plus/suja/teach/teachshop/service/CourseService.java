package plus.suja.teach.teachshop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.CourseRepository;
import plus.suja.teach.teachshop.entity.Course;
import plus.suja.teach.teachshop.enums.Status;
import plus.suja.teach.teachshop.exception.HttpException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseService {
    private CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    public Course getCourse(Integer id) {
        return courseRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new HttpException(401, "Not found"));
    }

    public Course createCourse(String title, String describe, BigDecimal price, HttpServletResponse response) {
        Course course = new Course();
        course.setTitle(title);
        course.setDescribe(describe);
        course.setPrice(price);
        Course saveCourse;
        try {
            saveCourse = courseRepository.save(course);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        response.setStatus(201);
        return saveCourse;
    }

    public Course modifyCourse(Integer id, String title, String describe, BigDecimal price, Status status) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new HttpException(401, "Not found"));
        course.setTitle(title);
        course.setDescribe(describe);
        course.setPrice(price);
        course.setStatus(status);
        courseRepository.save(course);
        return course;
    }

    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }
}
