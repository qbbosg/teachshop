package plus.suja.teach.teachshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Course;
import plus.suja.teach.teachshop.entity.Status;
import plus.suja.teach.teachshop.service.CourseService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CourseController {
    private CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course")
    public List<Course> getAllCourse() {
        return courseService.getAllCourse();
    }

    @GetMapping("/course/{id}")
    public Course getCourse(@PathVariable Integer id) {
        return courseService.getCourse(id);
    }

    @PostMapping("/course")
    public Course createCourse(@RequestParam String title,
                               @RequestParam String describe,
                               @RequestParam BigDecimal price,
                               HttpServletResponse response) {
        return courseService.createCourse(title, describe, price, response);
    }

    @PutMapping("/course/{id}")
    public Course modifyCourse(@PathVariable Integer id,
                               @RequestParam String title,
                               @RequestParam String describe,
                               @RequestParam BigDecimal price,
                               @RequestParam Status status) {
        return courseService.modifyCourse(id, title, describe, price, status);

    }
    @DeleteMapping("/course/{id}")
    public void deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);

    }
}
