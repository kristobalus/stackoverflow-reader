package com.voxtantum.soreader.api.entities;


import com.google.gson.annotations.SerializedName;


public class Error {

    @SerializedName("error_id")
    public Integer errorId;

    @SerializedName("error_message")
    public String errorMessage;

    @SerializedName("error_name")
    public String errorName;

}
