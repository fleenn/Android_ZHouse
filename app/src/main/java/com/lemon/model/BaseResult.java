package com.lemon.model;

/**
 * 项目名称:  [Lemon]
 * 包:        [com.lemon.model]
 * 类描述:    [简要描述]
 * 创建人:    [xflu]
 * 创建时间:  [2015/12/16 14:17]
 * 修改人:    [xflu]
 * 修改时间:  [2015/12/16 14:17]
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public class BaseResult<T> {
    protected String message;
    protected String resultCode = "";
    protected T data;
    protected String errorCode;
    protected BaseParam param;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseParam getParam() {
        return param;
    }

    public void setParam(BaseParam param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "message='" + message + '\'' +
                ", errorCode='" + resultCode + '\'' +
                ", data=" + data +
                '}';
    }
}
