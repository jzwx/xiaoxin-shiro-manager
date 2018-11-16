package com.xiaoxin.manager.common.utils;

/**
 * @Author:jzwx
 * @Desicription: IStatusMessage
 * @Date:Created in 2018-11-16 10:38
 * @Modified By:
 */
public interface IStatusMessage {

    String getCode();

    String getMessage();

    public enum SystemStatus implements IStatusMessage{

        SUCCESS("1000","SUCCESS"), //请求成功
        ERROR("1001","ERROR"),	   //请求失败
        PARAM_ERROR("1002","PARAM_ERROR"), //请求参数有误
        SUCCESS_MATCH("1003","SUCCESS_MATCH"), //表示成功匹配
        NO_LOGIN("1100","NO_LOGIN"), //未登录
        MANY_LOGINS("1101","MANY_LOGINS"), //多用户在线（踢出用户）
        UPDATE("1102","UPDATE"), //用户信息或权限已更新（退出重新登录）
        LOCK("1111","LOCK"); //用户已锁定
        private String code;
        private String message;

        private SystemStatus(String code,String message){
            this.code = code;
            this.message = message;
        }

        @Override
        public String getCode(){
            return this.code;
        }

        @Override
        public String getMessage(){
            return this.message;
        }
    }
}