package com.voxtantum.soreader.api.base;

public class ApiException extends Exception {

    public Integer httpCode;
    public String body;

    public ApiException(Integer httpCode, String body){
        super();
        this.httpCode = httpCode;
        this.body = body;
    }


}
