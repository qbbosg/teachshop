package plus.suja.teach.teachshop;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import plus.suja.teach.teachshop.entity.Member;
import plus.suja.teach.teachshop.entity.Session;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static plus.suja.teach.teachshop.config.HttpInterceptor.SESSION_ID;

public class AuthIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void registerNotFollowRequire() throws IOException, InterruptedException {
        // 注册用户
        HttpResponse<String> response;
        String body = "username=aaaa&password=wssaadd";
        response = post("/members/register", Map.of("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE), body);
        assertEquals(401, response.statusCode());
        assertEquals("{\"message\":\"用户名必须6-20之间\"}", response.body());


    }

    @Test
    public void illegalUserForbidLogin() throws IOException, InterruptedException {
        HttpResponse<String> response;
        String body = "username=wusong&password=wwssaadd";
        response = post("/members", getRequestFromHeads(), body);
        assertEquals(401, response.statusCode());
        assertTrue(response.body().contains("用户不存在"));

        //注册用户
        body = "username=wusong&password=wwssaadd";
        response = post("/members/register", getRequestFromHeads(), body);
        assertEquals(201, response.statusCode());

        //尝试登录
        body = "username=wusong&password=wwssaadds";

        response = post("/members", getRequestFromHeads(), body);

        assertEquals(401, response.statusCode());
        assertTrue(response.body().contains("密码错误"));
    }

    @Test
    public void registerLoginLoginOut() throws IOException, InterruptedException {
        //注册用户
        HttpResponse<String> response;
        String body = "username=wusong&password=wwssaadd";
        response = post("/members/register", getRequestFromHeads(), body);
        Member member = objectMapper.readValue(response.body(), Member.class);
        assertEquals(201, response.statusCode());
        assertEquals("wusong", member.getUsername());
        assertNull(member.getEncryptPassword());

        //用该用户进行登录
        response = post("/members", getRequestFromHeads(), body);
        member = objectMapper.readValue(response.body(), Member.class);
        String cookie = response.headers().firstValue("Set-Cookie").get();
        assertNotNull(cookie);
        assertTrue(cookie.contains(SESSION_ID));
        assertEquals(200, response.statusCode());
        assertEquals("wusong", member.getUsername());

        //确定该用户已经登录成功
        response = get("/members/online", cookie);
        Session session;
        session = objectMapper.readValue(response.body(), Session.class);
        assertEquals(200, response.statusCode());
        assertEquals("wusong", session.getMember().getUsername());

        //调用注销接口
        response = get("/members/offline", cookie);
        assertEquals(204, response.statusCode());

        //确定该用户已经登出
        response = get("/members/online", cookie);
        assertEquals(401, response.statusCode());
    }

    @Test
    public void getErrorIfUsernameAlreadyRegistered() throws IOException, InterruptedException {
        //注册一个用户
        HttpResponse<String> response;
        String body = "username=wusong&password=wwssaadd";
        response = post("/members/register", getRequestFromHeads(), body);
        assertEquals(201, response.statusCode());
        //再注册相同用户
        body = "username=wusong&password=wwssaadd";
        response = post("/members/register", getRequestFromHeads(), body);
        assertEquals(409, response.statusCode());
    }

    @Test
    public void allowAdminVisitAllMember() throws IOException, InterruptedException {
        assertEquals(200, get("/members", ADMIN_SESSION).statusCode());
    }

    @Test
    public void nonAllowNotAdminVisitAllMember() throws IOException, InterruptedException {
        assertEquals(403, get("/members", STUDENT_SESSION).statusCode());
        assertEquals(403, get("/members", TEACHER_SESSION).statusCode());
    }

    @Test
    public void clearDeletedMember() throws IOException, InterruptedException {
        assertEquals(403, delete("/members/1", TEACHER_SESSION).statusCode());
        assertEquals(403, delete("/members/clear/1", TEACHER_SESSION).statusCode());
        assertEquals(200, delete("/members/1", ADMIN_SESSION).statusCode());
        assertEquals(200, delete("/members/clear/1", ADMIN_SESSION).statusCode());
        assertEquals(404, get("/members/1", ADMIN_SESSION).statusCode());

    }


}
