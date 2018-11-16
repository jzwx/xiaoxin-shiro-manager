package com.xiaoxin.manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:jzwx
 * @Desicription: ShiroManagerPagehelperConfig
 * @Date:Created in 2018-11-13 15:41
 * @Modified By:
 */
@Configuration
@ConfigurationProperties(prefix = "mybatis.shiromanager.pagehelper")
public class ShiroManagerPagehelperConfig {
    /**
     * 增加了一个`params`参数来配置参数映射，用于从Map或ServletRequest中取值, 可以配置pageNum,pageSize,count,pageSizeZero,reasonable,orderBy
     */
    private String params;

    /**
     * 数据库方言
     */
    private String dialect;

    /**
     * 默认为false, 为true时，会将RowBounds第一个参数offset当成pageNum页码使用, 和startPage中的pageNum效果一样
     */
    private String offsetAsPageNum;

    /**
     * 默认为false, 为true时，使用RowBounds分页会进行count查询
     */
    private String rowBoundsWithCount;

    /**
     * 为true时，如果pageSize=0或者RowBounds.limit = 0就会查询出全部的结果
     */
    private String pageSizeZero;

    /**
     * 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页;禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
     */
    private String reasonable;

    /**
     * 支持通过Mapper接口参数来传递分页参数
     */
    private String supportMethodsArguments;

    /**
     * always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page
     */
    private String returnPageInfo;

    /**
     * Getter method for property <tt>params</tt>.
     *
     * @return property value of params
     */
    public String getParams() {
        return params;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param params value to be assigned to property params
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * Getter method for property <tt>dialect</tt>.
     *
     * @return property value of dialect
     */
    public String getDialect() {
        return dialect;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param dialect value to be assigned to property dialect
     */
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    /**
     * Getter method for property <tt>offsetAsPageNum</tt>.
     *
     * @return property value of offsetAsPageNum
     */
    public String getOffsetAsPageNum() {
        return offsetAsPageNum;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param offsetAsPageNum value to be assigned to property offsetAsPageNum
     */
    public void setOffsetAsPageNum(String offsetAsPageNum) {
        this.offsetAsPageNum = offsetAsPageNum;
    }

    /**
     * Getter method for property <tt>rowBoundsWithCount</tt>.
     *
     * @return property value of rowBoundsWithCount
     */
    public String getRowBoundsWithCount() {
        return rowBoundsWithCount;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param rowBoundsWithCount value to be assigned to property rowBoundsWithCount
     */
    public void setRowBoundsWithCount(String rowBoundsWithCount) {
        this.rowBoundsWithCount = rowBoundsWithCount;
    }

    /**
     * Getter method for property <tt>pageSizeZero</tt>.
     *
     * @return property value of pageSizeZero
     */
    public String getPageSizeZero() {
        return pageSizeZero;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param pageSizeZero value to be assigned to property pageSizeZero
     */
    public void setPageSizeZero(String pageSizeZero) {
        this.pageSizeZero = pageSizeZero;
    }

    /**
     * Getter method for property <tt>reasonable</tt>.
     *
     * @return property value of reasonable
     */
    public String getReasonable() {
        return reasonable;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param reasonable value to be assigned to property reasonable
     */
    public void setReasonable(String reasonable) {
        this.reasonable = reasonable;
    }

    /**
     * Getter method for property <tt>supportMethodsArguments</tt>.
     *
     * @return property value of supportMethodsArguments
     */
    public String getSupportMethodsArguments() {
        return supportMethodsArguments;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param supportMethodsArguments value to be assigned to property supportMethodsArguments
     */
    public void setSupportMethodsArguments(String supportMethodsArguments) {
        this.supportMethodsArguments = supportMethodsArguments;
    }

    /**
     * Getter method for property <tt>returnPageInfo</tt>.
     *
     * @return property value of returnPageInfo
     */
    public String getReturnPageInfo() {
        return returnPageInfo;
    }

    /**
     * Setter method for property <tt>counterType</tt>.
     *
     * @param returnPageInfo value to be assigned to property returnPageInfo
     */
    public void setReturnPageInfo(String returnPageInfo) {
        this.returnPageInfo = returnPageInfo;
    }
}
