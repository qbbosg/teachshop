package plus.suja.teach.teachshop;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import plus.suja.teach.teachshop.entity.PageResponse;
import plus.suja.teach.teachshop.entity.Video;
import plus.suja.teach.teachshop.enums.Status;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VideoTest extends AbstractIntegrationTest {
    @Test
    public void getAllVideo() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/videos");
        PageResponse<Video> page = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        assertEquals(3, page.getData().size());
        assertEquals("Java base", page.getData().get(0).getTitle());
    }

    @Test
    public void getVideo() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/videos/1");
        Video videos = objectMapper.readValue(response.body(), Video.class);
        assertEquals("Java base", videos.getTitle());
        assertEquals("for begion start java.", videos.getDescribe());
        assertEquals(new BigDecimal(10000), videos.getPrice());
        assertEquals(1, videos.getCourseId());
        assertEquals(Status.OK, videos.getStatus());
        assertTrue(videos.getLink().contains("start.mp4"));
    }

    @Test
    public void createVideo() throws IOException, InterruptedException {
        String body = "{\"status\":\"OK\",\"title\":\"Java high\",\"describe\":\"for begion start java.\",\"link\":\"/videos/high.mp4\",\"price\":10000,\"courseId\":1}";
        HttpResponse<String> response = post("/videos", TEACHER_SESSION, body);
        assertEquals(201, response.statusCode());
    }

    @Test
    public void modifyVideo() throws IOException, InterruptedException {
        String body = "{\"id\":1,\"status\":\"NO\",\"title\":\"Java base\",\"describe\":\"for begion start java.\",\"link\":\"/videos/start.mp4\",\"price\":10000,\"courseId\":1}";
        HttpResponse<String> response = put("/videos/1", TEACHER_SESSION, body);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void deleteVideo() throws IOException, InterruptedException {
        HttpResponse<String> response = delete("/videos/1", TEACHER_SESSION);
        assertEquals(200, response.statusCode());

    }
}
