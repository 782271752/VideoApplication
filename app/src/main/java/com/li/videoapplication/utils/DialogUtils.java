package com.li.videoapplication.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

/**
 * Created by li on 2014/7/29.
 */
public class DialogUtils {

    public static Dialog dialog=null;
    public static void showWaitDialog(Context context){
        dialog=new AlertDialog.Builder(context)
                .setView(new ProgressBar(context))
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void cancelWaitDialog(){
        dialog.cancel();
    }

    /**
     * 录音窗口
     */
    public Dialog recordDialog=null;
    public void showRecordDialog(Context context,String title){
        recordDialog=new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(new ProgressBar(context))
                .create();
        recordDialog.show();

    }

    public void cancelRecordDialog(){
        recordDialog.cancel();
    }
}
