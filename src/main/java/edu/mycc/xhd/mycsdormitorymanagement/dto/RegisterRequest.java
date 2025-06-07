package edu.mycc.xhd.mycsdormitorymanagement.dto;

/**
 * 注册请求DTO
 */
public class RegisterRequest {
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private String role = "USER"; // 默认为普通用户

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String realName, String email, String phone) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", realName='" + realName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}