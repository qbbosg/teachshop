package plus.suja.teach.teachshop.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.entity.BizContent;

@Service
public class PayService {

    @Value("${spring.alipay.app-private-key}")
    private String privateKey;
    @Value("${spring.alipay.public-key}")
    private String alipayPublicKey;
    @Value("${spring.alipay.url}")
    private String alipayServerUrl;
    @Value("${spring.alipay.appid}")
    private String appId;
    private ObjectMapper objectMapper;

    private AlipayClient alipayClient;

    @Autowired
    public PayService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void getAlipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(alipayServerUrl);
        alipayConfig.setAppId(appId);
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setSignType("RSA2");
        alipayClient = new DefaultAlipayClient(alipayConfig);
    }

    public AlipayTradePagePayResponse alipayTradePagePayRequest(BizContent bizContent) throws AlipayApiException, JsonProcessingException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //异步接收地址，仅支持http/https，公网可访问
//        request.setNotifyUrl("");
        //同步跳转地址，仅支持http/https
        request.setReturnUrl("http://localhost:8080/api/v1/orders/checkPay");
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        System.out.println(objectMapper.writeValueAsString(bizContent));
        return alipayClient.pageExecute(request, "POST");
    }

    public AlipayTradeQueryResponse alipayTradeQueryRequest(String outTradeNo) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        request.setBizModel(model);
        return alipayClient.execute(request);
    }

    public AlipayTradeCloseResponse alipayTradeCloseRequest(String outTradeNo) throws JsonProcessingException, AlipayApiException {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        BizContent bizContent = new BizContent();
        bizContent.setOutTradeNo(outTradeNo);
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        return alipayClient.execute(request);
    }
}
