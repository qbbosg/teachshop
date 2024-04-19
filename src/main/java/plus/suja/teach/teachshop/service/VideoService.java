package plus.suja.teach.teachshop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.annotation.Teacher;
import plus.suja.teach.teachshop.dao.VideoRepository;
import plus.suja.teach.teachshop.entity.PageResponse;
import plus.suja.teach.teachshop.entity.Video;
import plus.suja.teach.teachshop.exception.HttpException;

import java.util.List;

@Service
public class VideoService {
    private VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public PageResponse<Video> getAllVideos(Integer pageNum, Integer pageSize) {
        return new PageResponse<Video>().getAllPageResponse(pageNum, pageSize, videoRepository::findAll);
    }

    public List<Video> getAllVideosByCourseId(Integer courseId) {
        return videoRepository.findAllByCourseId(courseId);
    }

    public Video getVideos(Integer id) {
        return videoRepository.findById(id).orElseThrow(() -> new HttpException(401, "Not found"));
    }

    @Teacher
    public Video createVideos(Video video, HttpServletResponse response) {
        Video saveVideo = videoRepository.save(video);
        response.setStatus(201);
        return saveVideo;
    }

    @Teacher
    public void modifyVideos(Integer id, Video video) {
        videoRepository
                .findById(id)
                .ifPresent(hasVideo -> {
                    hasVideo.setTitle(video.getTitle());
                    hasVideo.setDescribe(video.getDescribe());
                    hasVideo.setPrice(video.getPrice());
                    hasVideo.setLink(video.getLink());
                    hasVideo.setStatus(video.getStatus());
                    videoRepository.save(hasVideo);
                });
    }

    @Teacher
    public void deleteVideos(Integer id) {
        videoRepository.deleteById(id);
    }
}
