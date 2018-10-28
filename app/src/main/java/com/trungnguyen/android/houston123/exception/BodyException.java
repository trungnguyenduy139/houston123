package com.trungnguyen.android.houston123.exception;

/**
 * Created by trungnd4 on 16/10/2018.
 */
public class BodyException extends Throwable {
    private String returnCode;

    private String message;

    public BodyException(String code, String msg) {
        super();
        this.returnCode = code;
        this.message = msg;
    }

    public String getReturnCode() {
        return returnCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
