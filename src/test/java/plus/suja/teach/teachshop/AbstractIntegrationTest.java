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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static plus.suja.teach.teachshop.config.HttpInterceptor.SESSION_ID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TeachShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test.properties"})
public class AbstractIntegrationTest {
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

    HttpResponse<String> post(String path, String accept, String contentType, String body) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(host + ":" + getPort() + apiVersion + path)).POST(HttpRequest.BodyPublishers.ofString(body));
        if (Strings.isNotEmpty(accept)) {
            builder.header("Accept", accept);
        }
        if (Strings.isNotEmpty(contentType)) {
            builder.header("Content-Type", contentType);
        }
        HttpRequest request = builder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    HttpResponse<String> get(String path, String cookie) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(host + ":" + getPort() + apiVersion + path));
        if (Strings.isNotEmpty(cookie)) {
            builder.header("Cookie", cookie);
        }
        HttpRequest request = builder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
