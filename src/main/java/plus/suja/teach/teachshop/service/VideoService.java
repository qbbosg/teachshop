package plus.suja.teach.teachshop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.annotation.Teacher;
import plus.suja.teach.teachshop.dao.VideoRepository;
import plus.suja.teach.teachshop.entity.Status;
import plus.suja.teach.teachshop.entity.Video;
import plus.suja.teach.teachshop.exception.HttpException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class VideoService {
    private VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Video getVideos(Integer id) {
        return videoRepository.findById(id).orElseThrow(() -> new HttpException(401, "Not found"));
    }

    @Teacher
    public Video createVideos(String title, String describe, String link, BigDecimal price, Integer courseId, HttpServletResponse response) {
        Video video = new Video();
        video.setTitle(title);
        video.setDescribe(describe);
        video.setLink(link);
        video.setPrice(price);
        video.setCourseId(courseId);
        Video saveVideo;
        try {
            saveVideo = videoRepository.save(video);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        response.setStatus(201);
        return saveVideo;
    }

    @Teacher
    public Video modifyVideos(Integer id, String title, String describe, String link, BigDecimal price, Status status) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new HttpException(401, "Not found"));
        video.setTitle(title);
        video.setDescribe(describe);
        video.setPrice(price);
        video.setLink(link);
        video.setStatus(status);
        videoRepository.save(video);
        return video;
    }

    @Teacher
    public void deleteVideos(Integer id) {
        videoRepository.deleteById(id);
    }
}
