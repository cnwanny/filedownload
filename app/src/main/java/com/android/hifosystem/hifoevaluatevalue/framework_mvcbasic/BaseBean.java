package com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic;

/**
 * 文件名： BaseBean
 * 功能：
 * 作者： wanny
 * 时间： 9:46 2016/8/15
 */
public class BaseBean<T> {
    //
    public boolean Status;
    public int Code;
    public String Message;
    public T Result;


    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        this.Result = result;
    }
}
