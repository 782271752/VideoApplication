package com.li.videoapplication.http;

import java.io.Serializable;

/**
 * Created by li on 2014/9/22.
 */
public class ResponseBase implements Serializable{

    public static final String TURE="true";
    public static final String FALSE="false";

    private String result;
    private String msg;
    private String data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
