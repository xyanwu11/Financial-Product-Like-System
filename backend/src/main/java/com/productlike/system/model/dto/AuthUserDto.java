package com.productlike.system.model.dto;

public class AuthUserDto {
    private String userId;
    private String userName;
    private String email;
    private String account;
    private String role;

    public AuthUserDto() {}

    public AuthUserDto(String userId, String userName, String email, String account, String role) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.account = account;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
