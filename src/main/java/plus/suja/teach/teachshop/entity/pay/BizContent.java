package plus.suja.teach.teachshop.entity.pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BizContent extends BizContentBase {
    private BigDecimal totalAmount;
    private String subject;
    private String productCode = "FAST_INSTANT_TRADE_PAY";
    private List<Goods> goodsDetails = new ArrayList<>();
    private String timeExpire;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProductCode() {
        return productCode;
    }

    public List<Goods> getGoodsDetails() {
        return goodsDetails;
    }

    public void setGoodsDetails(Goods goods) {
        this.goodsDetails.add(goods);
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }
}

