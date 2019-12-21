package com.changgou.common.model.response.system;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum  SystemCode implements ResultCode {
    SYSTEM_LOGIN_USERNAME_ERROR(false,26001,"请输入用户名称"),
    SYSTEM_LOGIN_PASSWORD_ERROR(false,26002,"请输入登录密码"),
    SYSTEM_LOGIN_UNAUTHORISE(false,26003,"无访问权限"),
    SYSTEM_LOGIN_USERNAMEORPASSWORD_ERROR(false,26004,"用户名或密码错误");

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    private SystemCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public int code() {
        return 0;
    }

    @Override
    public String message() {
        return null;
    }
}
