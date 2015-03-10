package com.li.videoapplication.http;

import android.util.Log;


import com.li.videoapplication.utils.HttpUtils;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by li on 2014/7/26.
 */
public class ServerHelper {

    public final static String ERRORCODE="result";
    public final static String MESSAGE="msg";
    public final static String DATASTR="data";

    /**
     * 请求服务器回应的数据
     * @param response 请求实体类
     * @param type  请求方式 0：get请求 1：post请求
     * @param param post数据
     * @param url 地址
     * @param <T>
     * @return
     */
    public static <T extends ResponseBase> DefaultMessageResponse<T> getResponseEntity(T response,int type,List<NameValuePair> param,String url){

        Log.e("url", url);

        String message="";
        JSONObject object=null;
        switch (type){
            case 0:
                message= HttpUtils.httpGet(url);
                message=message.replace("\\","");
                break;
            case 1:
                message=HttpUtils.httpPost(param,url);
//                message=message.substring(10,message.length()-1);
                Log.e("post参数",param.toString());
                message=message.replace("\\","");
                Log.e("post返回数据",message);
                break;
        }

        if (!message.equals("")){
            try {
                object = new JSONObject(message);
                response.setResult(object.getString(ERRORCODE));
                response.setMsg(object.getString(MESSAGE));
                response.setData(object.getString(DATASTR));

            }catch (JSONException e){

            }

            DefaultMessageResponse<T> rsp=new DefaultMessageResponse<T>(response);
            return rsp;
        }
        return null;
    }


}
