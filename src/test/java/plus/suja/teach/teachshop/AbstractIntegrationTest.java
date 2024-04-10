package plus.suja.teach.teachshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.util.Strings;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static plus.suja.teach.teachshop.config.HttpInterceptor.SESSION_ID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TeachShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test.properties"})
public abstract class AbstractIntegrationTest {
    public static final String SUPER_ADMIN_SESSION = SESSION_ID + "=super_admin";
    public static final String ADMIN_SESSION = SESSION_ID + "=admin";
    public static final String TEACHER_SESSION = SESSION_ID + "=teacher";
    public static final String STUDENT_SESSION = SESSION_ID + "=student1";
    @Autowired
    Environment environment;
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    @Value("${spring.datasource.password}")
    private String databasePassword;
    private String host = "http://localhost";
    private String apiVersion = "/api/v1";
    ObjectMapper objectMapper = new ObjectMapper();
    private HttpClient client = HttpClient.newHttpClient();

    AbstractIntegrationTest() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String getPort() {
        return environment.getProperty("local.server.port");
    }

    @BeforeEach
    public void resetDatabase() {
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    HttpResponse<String> post(String path, String body) throws IOException, InterruptedException {
        return request("POST", path, Map.of(), body);
    }

    HttpResponse<String> post(String path, Map<String, String> headers, String body) throws IOException, InterruptedException {
        return request("POST", path, headers, body);
    }

    HttpResponse<String> post(String path, String cookie, String body) throws IOException, InterruptedException {
        return request("POST", path, Map.of("Cookie", cookie), body);
    }

    HttpResponse<String> post(String path, String accept, String contentType, String cookie, String body) throws IOException, InterruptedException {
        return request("POST", path, Map.of("Cookie", cookie, "Accept", accept, "Content-Type", contentType), body);
    }

    HttpResponse<String> get(String path) throws IOException, InterruptedException {
        return request("GET", path, Map.of());
    }

    HttpResponse<String> get(String path, String cookie) throws IOException, InterruptedException {
        return request("GET", path, Map.of("Cookie", cookie));
    }

    HttpResponse<String> put(String path, String cookie, String body) throws IOException, InterruptedException {
        return request("PUT", path, Map.of("Cookie", cookie), body);
    }

    HttpResponse<String> delete(String path, String cookie, String body) throws IOException, InterruptedException {
        return request("DELETE", path, Map.of("Cookie", cookie), body);
    }


    private HttpResponse<String> request(String method, String path, Map<String, String> headers) throws IOException, InterruptedException {
        return request(method, path, headers, "");
    }

    private HttpResponse<String> request(String method, String path, Map<String, String> headers, String body) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(host + ":" + getPort() + apiVersion + path));
        headers.forEach(builder::header);
        if (!headers.containsKey("Content-Type")) {
            builder.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        }
        if (!headers.containsKey("Accept")) {
            builder.header("Accept", MediaType.APPLICATION_JSON_VALUE);
        }
        if (Strings.isEmpty(body)) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body));
        }
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
