package plus.suja.teach.teachshop.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.common.utils.StringUtils;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class AliyunOSSService {
    private String endpoint = "oss-cn-shanghai.aliyuncs.com";
    private String bucket = "course-suja";
    private String dir = "course-teacher/";
    private String host;
    private OSS client;

    AliyunOSSService() throws ClientException {
        host = "http://" + bucket + "." + endpoint;
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        client = new OSSClientBuilder().build(endpoint, credentialsProvider);
    }

    public Token getToken() {
        long expireTimeSeconds = 30;
        long expireEndTimeMillisSeconds = System.currentTimeMillis() + expireTimeSeconds * 1000;
        Date expiration = new Date(expireEndTimeMillisSeconds);
        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = client.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = client.calculatePostSignature(postPolicy);

        Token token = new Token();
        token.setAccessid(StringUtils.trim(System.getenv("OSS_ACCESS_KEY_ID")));
        token.setHost(host);
        token.setPolicy(encodedPolicy);
        token.setDir(dir);
        token.setSignature(postSignature);
        token.setExpire(expireEndTimeMillisSeconds);

        return token;
    }

    public String parseFileNameAndGetUrl(String filename) {
        return "mp4".contains(filename.split(".")[1]) ? getVideoUrl(filename) : getFile(filename);
    }

    public String getVideoUrl(String filename) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000L);
        return client.generatePresignedUrl(bucket, dir + filename, expiration).toString();
    }

    public String getFile(String filename) {
        Date expiration = new Date(new Date().getTime() + 3600 * 1000L);
        return client.generatePresignedUrl(bucket, dir + filename, expiration).toString();
    }

    public List<OSSObjectSummary> files() {
        // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
        ObjectListing objectListing = client.listObjects(bucket);
        // objectListing.getObjectSummaries获取所有文件的描述信息。
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        return objectListing.getObjectSummaries();
    }

    public String downloadFile(String filename, String savePath) {
        String userDirectory = Paths.get("").toAbsolutePath().toString();
        client.getObject(new GetObjectRequest(bucket, dir + filename), new File(userDirectory + savePath + filename));
        return "download";
    }

    public static class Token {
        private String accessid;
        private String policy;
        private String signature;
        private String dir;
        private String host;
        private long expire;

        public String getAccessid() {
            return accessid;
        }

        public void setAccessid(String accessid) {
            this.accessid = accessid;
        }

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public long getExpire() {
            return expire;
        }

        public void setExpire(long expire) {
            this.expire = expire;
        }
    }
}

