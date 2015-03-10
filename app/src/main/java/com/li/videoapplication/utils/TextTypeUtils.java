package com.li.videoapplication.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTypeUtils {

	/**
	 * 获取字体样式
	 * @param context
	 * @return
	 */
	public static Typeface getTypeface(Context context){
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/themify.ttf");
		return typeface;
	}



    /**
     * 判断是否邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 判断昵称是否合法
     * @param nickname
     * @return
     */
    public static boolean isNameCompete(String nickname){
        String regEx="[`~!@#$%^\\\\\\-&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(nickname);
        if (m.find()) {
            return false;
        }else{
            return true;
        }
    }
}
