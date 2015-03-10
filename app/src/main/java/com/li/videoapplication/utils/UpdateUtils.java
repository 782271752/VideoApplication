package com.li.videoapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;

/**
 * 升级工具类
 * Created by li on 2014/10/18.
 */
public class UpdateUtils {

    public static int localVersion = 0;// 本地安装版本
    /**
     * 获取本地apk版本号
     * @param context
     * @return
     */
    public static int getLocalVersionCode(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            localVersion = packageInfo.versionCode;
            Log.e("本地apk版本", localVersion + "");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return localVersion;
    }

    public static void showVersionDialog(final String url,final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("检测到新版本更新,是否立刻更新？");

        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(url);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(it);
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

}
