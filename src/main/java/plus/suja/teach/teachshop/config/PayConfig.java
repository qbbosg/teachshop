package plus.suja.teach.teachshop.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class PayConfig {

    @Value("${spring.alipay.app-private-key}")
    private String privateKey;
    @Value("${spring.alipay.public-key}")
    private String alipayPublicKey;
    @Value("${spring.alipay.url}")
    private String alipayServerUrl;
    @Value("${spring.alipay.appid}")
    private String appId;
    private AlipayClient client;

    @PostConstruct
    public void setConfig() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(alipayServerUrl);
        alipayConfig.setAppId(appId);
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setSignType("RSA2");
        client = new DefaultAlipayClient(alipayConfig);
    }

    public AlipayClient getClient() {
        return client;
    }
}
