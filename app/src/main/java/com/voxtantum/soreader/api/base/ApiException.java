package com.voxtantum.soreader.api.base;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.voxtantum.soreader.api.entities.Error;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ApiException extends Exception {

    public Integer httpCode;
    public String message;
    public Error error;

    public ApiException(Integer httpCode, String message, ResponseBody errorBody){
        super();
        this.httpCode = httpCode;
        this.message = message;

        if (errorBody != null){
            Gson gson = new Gson();
            try {
                this.error = gson.fromJson(errorBody.string(), Error.class);
            } catch (JsonSyntaxException | IOException e){
                // do nothing
            }
        }

    }


}
