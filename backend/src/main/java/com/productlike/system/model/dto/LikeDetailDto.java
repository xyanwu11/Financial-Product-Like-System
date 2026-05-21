package com.productlike.system.model.dto;

import java.math.BigDecimal;

public class LikeDetailDto {
    private Long sn;
    private String userId;
    private String userName;
    private String productName;
    private BigDecimal price;
    private BigDecimal feeRate;
    private Integer purchaseQuantity;
    private String account;
    private BigDecimal totalFee;
    private BigDecimal totalAmount;
    private String email;

    public Long getSn() { return sn; }
    public void setSn(Long sn) { this.sn = sn; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getFeeRate() { return feeRate; }
    public void setFeeRate(BigDecimal feeRate) { this.feeRate = feeRate; }

    public Integer getPurchaseQuantity() { return purchaseQuantity; }
    public void setPurchaseQuantity(Integer purchaseQuantity) { this.purchaseQuantity = purchaseQuantity; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public BigDecimal getTotalFee() { return totalFee; }
    public void setTotalFee(BigDecimal totalFee) { this.totalFee = totalFee; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
