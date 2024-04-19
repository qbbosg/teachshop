package plus.suja.teach.teachshop;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import plus.suja.teach.teachshop.entity.Course;
import plus.suja.teach.teachshop.entity.PageResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseTest extends AbstractIntegrationTest {
    @Test
    public void getAllCourse() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/course");
        PageResponse<Course> page = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        assertEquals(1, page.getTotalPage());
        assertEquals(1, page.getData().size());
        assertEquals("Java collection", page.getData().get(0).getTitle());
    }

    @Test
    public void getCourse() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/course/1");
        Course course = objectMapper.readValue(response.body(), Course.class);
        assertEquals(1, course.getId());
        assertEquals(new BigDecimal(99999), course.getPrice());
        assertEquals("Java collection", course.getTitle());
        assertTrue(course.getDescribe().contains("all java teach"));
    }

    @Test
    public void createCourse() throws IOException, InterruptedException {
        String body = "title=python&describe=happy learn python&price=998";
        HttpResponse<String> response = post("/course", MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE, TEACHER_SESSION, body);
        assertEquals(201, response.statusCode());
    }

    @Test
    public void modifyCourse() throws IOException, InterruptedException {
        String body = "title=python&describe=happy learn python&price=998&status=NO";
        HttpResponse<String> response = put("/course/1", TEACHER_SESSION, MediaType.APPLICATION_FORM_URLENCODED_VALUE, body);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void deleteCourse() throws IOException, InterruptedException {
        HttpResponse<String> response = delete("/course/1", TEACHER_SESSION);
        assertEquals(200, response.statusCode());
    }

}
