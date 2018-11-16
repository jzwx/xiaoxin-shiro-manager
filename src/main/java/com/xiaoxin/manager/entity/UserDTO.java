package com.xiaoxin.manager.entity;

import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;

/**
 * @Author:jzwx
 * @Desicription: UserDTO
 * @Date:Created in 2018-11-16 10:55
 * @Modified By:
 */
public class UserDTO {

    private Integer id;
    @NotNull(message = "用户名不能为空，请您先填写用户名")
    private String username;
//    @NotNull(message = "手机号不能为空，请您先填写手机号")
    private String mobile;

    private String email;
    @NotNull(message = "密码不能为空，请您先填写手机号")
    @MatchPattern(pattern = "^[0-9_a-zA-Z]{6,20}$", message = "用户名或密码有误，请您重新填写")
    private String password;
    @NotNull(message = "图片验证码不能为空，请您先填写验证码")
    @MatchPattern(pattern = "\\w{4}$", message = "图片验证码有误，请您重新填写")
    private String code;

    /**
     * Getter method for property <tt>id</tt>.
     *
     * @return property value of id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param id value to be assigned to property id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>username</tt>.
     *
     * @return property value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param username value to be assigned to property username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method for property <tt>mobile</tt>.
     *
     * @return property value of mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param mobile value to be assigned to property mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Getter method for property <tt>email</tt>.
     *
     * @return property value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param email value to be assigned to property email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter method for property <tt>password</tt>.
     *
     * @return property value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param password value to be assigned to property password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param code value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
