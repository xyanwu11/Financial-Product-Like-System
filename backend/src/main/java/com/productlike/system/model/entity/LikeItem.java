package com.productlike.system.model.entity;

import java.math.BigDecimal;

public class LikeItem {
    private Long sn;
    private String userId;
    private Long productNo;
    private Integer purchaseQuantity;
    private String account;
    private BigDecimal totalFee;
    private BigDecimal totalAmount;

    public Long getSn() { return sn; }
    public void setSn(Long sn) { this.sn = sn; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getProductNo() { return productNo; }
    public void setProductNo(Long productNo) { this.productNo = productNo; }

    public Integer getPurchaseQuantity() { return purchaseQuantity; }
    public void setPurchaseQuantity(Integer purchaseQuantity) { this.purchaseQuantity = purchaseQuantity; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public BigDecimal getTotalFee() { return totalFee; }
    public void setTotalFee(BigDecimal totalFee) { this.totalFee = totalFee; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
