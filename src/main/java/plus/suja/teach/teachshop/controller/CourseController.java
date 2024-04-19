package plus.suja.teach.teachshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.Course;
import plus.suja.teach.teachshop.entity.PageResponse;
import plus.suja.teach.teachshop.service.CourseService;

@RestController
@RequestMapping("/api/v1")
public class CourseController {
    private CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course")
    public PageResponse<Course> getAllCourse(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return new PageResponse<Course>().checkPageAndFunctionApply(pageNum, pageSize, courseService::getAllCourse);
    }

    @GetMapping("/course/{id}")
    public Course getCourse(@PathVariable Integer id) {
        return courseService.getCourse(id);
    }

    @PostMapping("/course")
    public Course createCourse(@RequestBody Course course, HttpServletResponse response) {
        return courseService.createCourse(course, response);
    }

    @PutMapping("/course/{id}")
    public void modifyCourse(@PathVariable Integer id, @RequestBody Course course) {
        courseService.modifyCourse(id, course);
    }

    @DeleteMapping("/course/{id}")
    public void deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);

    }
}
