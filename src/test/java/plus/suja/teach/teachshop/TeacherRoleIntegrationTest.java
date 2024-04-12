package plus.suja.teach.teachshop;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Status;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherRoleIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void getAllStudents() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/members/students", TEACHER_SESSION);
        List<Member> students = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        assertEquals(200, response.statusCode());
        assertEquals(2, students.size());
        assertEquals("li", students.get(0).getUsername());
    }

    @Test
    public void createStudent() throws IOException, InterruptedException {
        String body = "username=lidabao&password=hweksxds";
        assertEquals(403, post("/members/students",
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                STUDENT_SESSION,
                body).statusCode());
        HttpResponse<String> response = post("/members/students",
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                TEACHER_SESSION,
                body);

        assertEquals(201, response.statusCode());
        Member student = objectMapper.readValue(response.body(), Member.class);
        assertEquals("lidabao", student.getUsername());
    }

    @Test
    public void getStudent() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/members/students/2", TEACHER_SESSION);
        Member member = objectMapper.readValue(response.body(), Member.class);
        assertEquals(200, response.statusCode());
        assertEquals("wu", member.getUsername());
        assertEquals(404, get("/members/students/222", TEACHER_SESSION).statusCode());
    }

    @Test
    public void updateStudent() throws IOException, InterruptedException {
        String body = "username=liqing&status=OK";
        HttpResponse<String> response = put("/members/students/2", TEACHER_SESSION, MediaType.APPLICATION_FORM_URLENCODED_VALUE, body);
        Member member = objectMapper.readValue(response.body(), Member.class);
        assertEquals(200, response.statusCode());
        assertEquals("liqing", member.getUsername());
        assertEquals(Status.OK, member.getStatus());
    }

    @Test
    public void deleteStudent() throws IOException, InterruptedException {
        assertEquals(200, delete("/members/students/1", TEACHER_SESSION).statusCode());
        assertEquals(403, delete("/members/students/1", STUDENT_SESSION).statusCode());
    }
}
