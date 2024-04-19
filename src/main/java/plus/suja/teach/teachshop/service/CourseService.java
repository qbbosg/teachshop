package plus.suja.teach.teachshop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.dao.CourseRepository;
import plus.suja.teach.teachshop.entity.Course;
import plus.suja.teach.teachshop.entity.PageResponse;
import plus.suja.teach.teachshop.exception.HttpException;

@Service
public class CourseService {
    private CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public PageResponse<Course> getAllCourse(Integer pageNum, Integer pageSize) {
        return new PageResponse<Course>().getAllPageResponse(pageNum, pageSize, courseRepository::findAll);
    }


    public Course getCourse(Integer id) {
        return courseRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new HttpException(401, "Not found"));
    }

    public Course createCourse(Course course, HttpServletResponse response) {
        Course saveCourse = courseRepository.save(course);
        response.setStatus(201);
        return saveCourse;
    }

    public void modifyCourse(Integer id, Course course) {
        courseRepository
                .findById(id)
                .ifPresent(hasCourse -> {
                    hasCourse.setTitle(course.getTitle());
                    hasCourse.setPrice(course.getPrice());
                    hasCourse.setDescribe(course.getDescribe());
                    hasCourse.setStatus(course.getStatus());
                    courseRepository.save(hasCourse);
                });
    }

    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }
}
