package com.railwayservice.application.vo;

/**
 * 登陆返回数据。
 *
 * @author Ewing
 * @date 2017/3/3
 */
public class LoginMessage {
    private String salt;
    private String token;

    public LoginMessage() {
    }

    public LoginMessage(String salt) {
        this.salt = salt;
    }

    public LoginMessage(String salt, String token) {
        this.salt = salt;
        this.token = token;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
