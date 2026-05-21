package com.productlike.system.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LikeRequest {

    @NotBlank(message = "使用者ID不可為空")
    private String userId;

    @NotNull(message = "產品流水號不可為空")
    private Long productNo;

    @NotNull(message = "購買數量不可為空")
    @Min(value = 1, message = "購買數量至少為1")
    private Integer purchaseQuantity;

    @NotBlank(message = "扣款帳號不可為空")
    private String account;

    private String productName;
    private BigDecimal price;
    private BigDecimal feeRate;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getProductNo() { return productNo; }
    public void setProductNo(Long productNo) { this.productNo = productNo; }

    public Integer getPurchaseQuantity() { return purchaseQuantity; }
    public void setPurchaseQuantity(Integer purchaseQuantity) { this.purchaseQuantity = purchaseQuantity; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getFeeRate() { return feeRate; }
    public void setFeeRate(BigDecimal feeRate) { this.feeRate = feeRate; }
}
