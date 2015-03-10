package com.li.videoapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.li.videoapplication.entity.UserEntity;

import org.json.JSONObject;


/**数据保存类
 * Created by li on 2014/9/24.
 */
public class SharePreferenceUtil {


    public static void setPreference(Context context, String key,String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        editor = null;
    }

    public static String getPreference(Context context,String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString(key, "");
        return value;
    }

    /**
     * 设置保存登录用户信息
     */
    public static void setUserEntity(Context context, String json) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("User", json);
        Log.e("user",json);
        editor.commit();
        editor = null;
    }


    /**
     * 获取登录用户信息
     */
    public static UserEntity getUserEntity(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("User", "");
        String result = "";
        JSONObject dasta=new JSONObject();
        Log.e("user",json);
        UserEntity user=new UserEntity();
        try{
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals("true")) {
                return null;
            }
            dasta=object.getJSONObject("data");
            if (dasta.toString().equals("false")){
                return null;
            }
            user.setImgPath(dasta.getString("avatar"));
            user.setId(dasta.getString("id"));
            user.setName(dasta.getString("name"));
            user.setTitle(dasta.getString("nickname"));
            user.setOpenId(dasta.getString("openid"));
            if (dasta.has("address")){
                user.setAddress(dasta.getString("address"));
            }

            if (dasta.has("degree")) {
                user.setGrace(dasta.getString("degree"));
            }
            if (dasta.has("isAdmin")) {
                user.setIsAdmin(dasta.getString("isAdmin"));
            }
            if (dasta.has("like_gametype")) {
                user.setLike_gametype(dasta.getString("like_gametype"));
            }
            if (dasta.has("mobile")) {
                user.setMobile(dasta.getString("mobile"));
            }


            user.setPassword(dasta.getString("password"));
            user.setRank(dasta.getString("rank"));
            user.setSex(dasta.getString("sex"));
            user.setTime(dasta.getString("time"));
            return user;
        }catch (Exception e){
            Log.e("getUerInfo",e.toString());
        }
        return null;
    }
}
