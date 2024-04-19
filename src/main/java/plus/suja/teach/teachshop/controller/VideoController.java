package plus.suja.teach.teachshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.enums.Status;
import plus.suja.teach.teachshop.entity.Video;
import plus.suja.teach.teachshop.service.VideoService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VideoController {
    private VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/videos")
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/videos/{id}")
    public Video getVideos(@PathVariable Integer id) {
        return videoService.getVideos(id);
    }

    @PostMapping("/videos")
    public Video createVideos(@RequestParam String title,
                              @RequestParam String describe,
                              @RequestParam String link,
                              @RequestParam BigDecimal price,
                              @RequestParam Integer courseId,
                              HttpServletResponse response) {
        return videoService.createVideos(title, describe, link, price, courseId, response);
    }

    @PutMapping("/videos/{id}")
    public Video modifyVideos(@PathVariable Integer id,
                             @RequestParam String title,
                             @RequestParam String describe,
                             @RequestParam String link,
                             @RequestParam BigDecimal price,
                             @RequestParam Status status) {
        return videoService.modifyVideos(id, title, describe, link, price, status);
    }
    @DeleteMapping("/videos/{id}")
    public void deleteVideos(@PathVariable Integer id) {
        videoService.deleteVideos(id);
    }

}
