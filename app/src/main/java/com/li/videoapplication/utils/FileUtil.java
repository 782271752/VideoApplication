package com.li.videoapplication.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by li on 2014/10/12.
 */
public class FileUtil {

    private static String state = Environment.getExternalStorageState();
    public static File updateFile;

    /***
     * 创建文件
     */
    public static void createFile(String name) {
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File updateDir = new File(Environment.getExternalStorageDirectory()+"/SYSJ/video/");
            updateFile = new File(updateDir + name + ".swf");

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }

            if (updateFile.exists()) {
                updateFile.delete();
            }

    //			if (!updateFile.exists()) {
            try {
                updateFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
