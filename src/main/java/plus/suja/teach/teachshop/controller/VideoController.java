package plus.suja.teach.teachshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.entity.PageResponse;
import plus.suja.teach.teachshop.entity.Video;
import plus.suja.teach.teachshop.service.VideoService;

@RestController
@RequestMapping("/api/v1")
public class VideoController {
    private VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/videos")
    public PageResponse<Video> getAllVideos(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return new PageResponse<Video>().checkPageAndFunctionApply(pageNum, pageSize, videoService::getAllVideos);
    }

    @GetMapping("/videos/{id}")
    public Video getVideos(@PathVariable Integer id) {
        return videoService.getVideos(id);
    }

    @PostMapping("/videos")
    public Video createVideos(@RequestBody Video video,
                              HttpServletResponse response) {
        return videoService.createVideos(video, response);
    }

    @PutMapping("/videos/{id}")
    public void modifyVideos(@PathVariable Integer id, @RequestBody Video video) {
        videoService.modifyVideos(id, video);
    }

    @DeleteMapping("/videos/{id}")
    public void deleteVideos(@PathVariable Integer id) {
        videoService.deleteVideos(id);
    }

}
