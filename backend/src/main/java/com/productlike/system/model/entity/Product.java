package com.productlike.system.model.entity;

import java.math.BigDecimal;

public class Product {
    private Long no;
    private String productName;
    private BigDecimal price;
    private BigDecimal feeRate;

    public Long getNo() { return no; }
    public void setNo(Long no) { this.no = no; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getFeeRate() { return feeRate; }
    public void setFeeRate(BigDecimal feeRate) { this.feeRate = feeRate; }
}
