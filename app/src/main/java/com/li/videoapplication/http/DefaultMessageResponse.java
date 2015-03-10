package com.li.videoapplication.http;

/**
 * Created by li on 2014/9/22.
 */
public class DefaultMessageResponse<T extends  ResponseBase> {

    private T body;

    public DefaultMessageResponse(T body){
        this.body=body;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
