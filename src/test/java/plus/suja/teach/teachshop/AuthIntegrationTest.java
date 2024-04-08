package plus.suja.teach.teachshop;

import org.junit.jupiter.api.Test;
import plus.suja.teach.teachshop.entity.Member;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static plus.suja.teach.teachshop.config.Interceptor.MemberInterceptor.SESSION_ID;

public class AuthIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void registerInfoNotFollowRequire() throws IOException, InterruptedException {
        // 注册用户
        HttpResponse<String> response;
        String body = "username=aaaa&password=wssaadd";

        response = post("/members/register", "application/json", "application/x-www-form-urlencoded", body);

        assertEquals(401, response.statusCode());
        assertEquals("{\"message\":\"用户名必须6-20之间\"}", response.body());


    }

    @Test
    public void illegalUserForbidLogin() throws IOException, InterruptedException {
        HttpResponse<String> response;
        String body = "username=wusong&password=wwssaadd";

        response = post("/members", "application/json", "application/x-www-form-urlencoded", body);

        assertEquals(401, response.statusCode());
        assertTrue(response.body().contains("用户不存在"));

        //注册用户
        body = "username=wusong&password=wwssaadd";

        response = post("/members/register", "application/json", "application/x-www-form-urlencoded", body);

        assertEquals(201, response.statusCode());

        //尝试登录
        body = "username=wusong&password=wwssaadds";

        response = post("/members", "application/json", "application/x-www-form-urlencoded", body);

        assertEquals(401, response.statusCode());
        assertTrue(response.body().contains("密码错误"));
    }

    @Test
    public void registerLoginLoginOut() throws IOException, InterruptedException {
        //注册用户
        HttpResponse<String> response;
        String body = "username=wusong&password=wwssaadd";

        response = post("/members/register", "application/json", "application/x-www-form-urlencoded", body);
        Member member = objectMapper.readValue(response.body(), Member.class);

        assertEquals(201, response.statusCode());
        assertEquals("wusong", member.getUsername());
        assertNull(member.getEncryptPassword());

        //用该用户进行登录
        response = post("/members", "application/json", "application/x-www-form-urlencoded", body);
        member = objectMapper.readValue(response.body(), Member.class);
        String setCookie = response.headers().firstValue("Set-Cookie").get();

        assertNotNull(setCookie);
        assertTrue(setCookie.contains(SESSION_ID));
        assertEquals(200, response.statusCode());
        assertEquals("wusong", member.getUsername());
        //确定该用户已经登录成功


        //调用注销接口
        //确定该用户已经登出
    }
}
