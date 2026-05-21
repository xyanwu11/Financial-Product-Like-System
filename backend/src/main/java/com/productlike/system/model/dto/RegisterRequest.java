package com.productlike.system.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "請輸入使用者 ID")
    @Size(max = 20, message = "使用者 ID 最多 20 字")
    private String userId;

    @NotBlank(message = "請輸入姓名")
    @Size(max = 50, message = "姓名最多 50 字")
    private String userName;

    @NotBlank(message = "請輸入 Email")
    @Email(message = "Email 格式不正確")
    @Size(max = 100, message = "Email 最多 100 字")
    private String email;

    @NotBlank(message = "請輸入扣款帳號")
    @Size(max = 20, message = "扣款帳號最多 20 字")
    private String account;

    @NotBlank(message = "請輸入密碼")
    @Size(min = 4, max = 100, message = "密碼長度需介於 4 到 100 字")
    private String password;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
