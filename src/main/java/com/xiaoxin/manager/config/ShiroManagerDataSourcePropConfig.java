package com.xiaoxin.manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:jzwx
 * @Desicription: ShiroManagerDataSourcePropConfig
 * @Date:Created in 2018-11-13 15:44
 * @Modified By:
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.shiromanager")
public class ShiroManagerDataSourcePropConfig {
    private String driverClassName;

    private String url;
    private String username;
    private String password;
    private String type;
    /** 连接池的补充设置，应用到上面所有数据源中# 初始化大小，最小，最大 */
    private String initialSize;
    private String minIdle;
    private String maxActive;

    /** 配置获取连接等待超时的时间 */
    private String maxWait;

    /** 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
    private String timeBetweenEvictionRunsMillis;
    /** 配置一个连接在池中最小生存的时间，单位是毫秒 */
    private String minEvictableIdleTimeMillis;
    private String validationQuery;
    private String testWhileIdle;
    private String testOnBorrow;
    private String testOnReturn;

    /** 打开PSCache，并且指定每个连接上PSCache的大小 */
    private String poolPreparedStatements;
    private String maxPoolPreparedStatementPerConnectionSize;

    /**
     * Getter method for property <tt>driverClassName</tt>.
     *
     * @return property value of driverClassName
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param driverClassName value to be assigned to property driverClassName
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * Getter method for property <tt>url</tt>.
     *
     * @return property value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param url value to be assigned to property url
     */
    public void setUrl(String url) {
        this.url = url;
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
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param type value to be assigned to property type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter method for property <tt>initialSize</tt>.
     *
     * @return property value of initialSize
     */
    public String getInitialSize() {
        return initialSize;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param initialSize value to be assigned to property initialSize
     */
    public void setInitialSize(String initialSize) {
        this.initialSize = initialSize;
    }

    /**
     * Getter method for property <tt>minIdle</tt>.
     *
     * @return property value of minIdle
     */
    public String getMinIdle() {
        return minIdle;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param minIdle value to be assigned to property minIdle
     */
    public void setMinIdle(String minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * Getter method for property <tt>maxActive</tt>.
     *
     * @return property value of maxActive
     */
    public String getMaxActive() {
        return maxActive;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param maxActive value to be assigned to property maxActive
     */
    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * Getter method for property <tt>maxWait</tt>.
     *
     * @return property value of maxWait
     */
    public String getMaxWait() {
        return maxWait;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param maxWait value to be assigned to property maxWait
     */
    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * Getter method for property <tt>timeBetweenEvictionRunsMillis</tt>.
     *
     * @return property value of timeBetweenEvictionRunsMillis
     */
    public String getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param timeBetweenEvictionRunsMillis value to be assigned to property timeBetweenEvictionRunsMillis
     */
    public void setTimeBetweenEvictionRunsMillis(String timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * Getter method for property <tt>minEvictableIdleTimeMillis</tt>.
     *
     * @return property value of minEvictableIdleTimeMillis
     */
    public String getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param minEvictableIdleTimeMillis value to be assigned to property minEvictableIdleTimeMillis
     */
    public void setMinEvictableIdleTimeMillis(String minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * Getter method for property <tt>validationQuery</tt>.
     *
     * @return property value of validationQuery
     */
    public String getValidationQuery() {
        return validationQuery;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param validationQuery value to be assigned to property validationQuery
     */
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    /**
     * Getter method for property <tt>testWhileIdle</tt>.
     *
     * @return property value of testWhileIdle
     */
    public String getTestWhileIdle() {
        return testWhileIdle;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param testWhileIdle value to be assigned to property testWhileIdle
     */
    public void setTestWhileIdle(String testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    /**
     * Getter method for property <tt>testOnBorrow</tt>.
     *
     * @return property value of testOnBorrow
     */
    public String getTestOnBorrow() {
        return testOnBorrow;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param testOnBorrow value to be assigned to property testOnBorrow
     */
    public void setTestOnBorrow(String testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    /**
     * Getter method for property <tt>testOnReturn</tt>.
     *
     * @return property value of testOnReturn
     */
    public String getTestOnReturn() {
        return testOnReturn;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param testOnReturn value to be assigned to property testOnReturn
     */
    public void setTestOnReturn(String testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    /**
     * Getter method for property <tt>poolPreparedStatements</tt>.
     *
     * @return property value of poolPreparedStatements
     */
    public String getPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param poolPreparedStatements value to be assigned to property poolPreparedStatements
     */
    public void setPoolPreparedStatements(String poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    /**
     * Getter method for property <tt>maxPoolPreparedStatementPerConnectionSize</tt>.
     *
     * @return property value of maxPoolPreparedStatementPerConnectionSize
     */
    public String getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param maxPoolPreparedStatementPerConnectionSize value to be assigned to property maxPoolPreparedStatementPerConnectionSize
     */
    public void setMaxPoolPreparedStatementPerConnectionSize(String maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }
}
