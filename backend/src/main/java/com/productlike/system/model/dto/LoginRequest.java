package com.productlike.system.model.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "請輸入使用者 ID")
    private String userId;

    @NotBlank(message = "請輸入密碼")
    private String password;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
