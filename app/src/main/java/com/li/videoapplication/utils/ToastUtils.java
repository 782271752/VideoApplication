package com.li.videoapplication.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by li on 2014/9/23.
 */
public class ToastUtils {
    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
