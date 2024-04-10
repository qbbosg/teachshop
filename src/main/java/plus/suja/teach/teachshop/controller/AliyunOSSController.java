package plus.suja.teach.teachshop.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.suja.teach.teachshop.service.AliyunOSSService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aliyunoss")
public class AliyunOSSController {
    @Autowired
    private AliyunOSSService aliyunOSSService;

    @GetMapping("/token")
    public AliyunOSSService.Token token() {
        return aliyunOSSService.getToken();
    }

    @GetMapping("/video/{filename}")
    public String getVideoUrl(@PathVariable("filename") String filename) {
        return aliyunOSSService.parseFileNameAndGetUrl(filename);
    }

    @GetMapping("/files")
    public List<OSSObjectSummary> getFileList() {
        return aliyunOSSService.files();
    }

    @GetMapping("/files/download/{filename}")
    public String downloadFile(@PathVariable String filename) {
        String relativeProjectPath = "/src/main/resources/static/images/";

        return aliyunOSSService.downloadFile(filename, relativeProjectPath);
    }
}
