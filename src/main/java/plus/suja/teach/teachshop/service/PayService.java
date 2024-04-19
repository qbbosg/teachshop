package plus.suja.teach.teachshop.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.suja.teach.teachshop.config.PayConfig;
import plus.suja.teach.teachshop.entity.pay.BizContent;
import plus.suja.teach.teachshop.entity.pay.BizContentBase;
import plus.suja.teach.teachshop.entity.pay.Refund;

import java.math.BigDecimal;


@Service
public class PayService extends PayConfig {
    AlipayClient alipayClient = new PayConfig().getClient();


    private ObjectMapper objectMapper;


    @Autowired
    public PayService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public AlipayTradePagePayResponse alipayTradePagePayRequest(BizContent bizContent) throws AlipayApiException, JsonProcessingException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //异步接收地址，仅支持http/https，公网可访问
//        request.setNotifyUrl("");
        //同步跳转地址，仅支持http/https
        request.setReturnUrl("http://localhost:8080/api/v1/orders/checkPay");
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
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
        BizContentBase bizContent = new BizContentBase();
        bizContent.setOutTradeNo(outTradeNo);
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        return alipayClient.execute(request);
    }

    public AlipayTradeRefundResponse alipayTradeRefundRequest(String outTradeNo, BigDecimal amount) throws JsonProcessingException, AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        Refund bizContent = new Refund();
        bizContent.setOutTradeNo(outTradeNo);
        bizContent.setRefundAmount(amount.divide(new BigDecimal(100)));
        bizContent.setRefundReason("我不想要了");
        request.setBizContent(objectMapper.writeValueAsString(bizContent));
        return alipayClient.execute(request);
    }
}
