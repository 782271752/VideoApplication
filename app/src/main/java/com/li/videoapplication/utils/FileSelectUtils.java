package com.li.videoapplication.utils;

import android.os.Environment;

import com.li.videoapplication.entity.LocalFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** 文件遍历类
 * Created by li on 2014/10/22.
 */
public class FileSelectUtils {
    private static String state = Environment.getExternalStorageState();

    /**
     * 获取本地文件
     * @return
     */
    public static List<LocalFile> getFileList(){
        List<LocalFile> localFiles=new ArrayList<LocalFile>();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File f = new File(Environment.getExternalStorageDirectory()+"/SupperLulu");
            File[] files = f.listFiles();// 列出所有文件
            if(files != null){
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++) {
                    File file = files[i];
//                    items.add(file.getName());
//                    paths.add(file.getPath());
                    LocalFile localFile=new LocalFile();
                    localFile.setName(file.getName());

                    localFile.setPath(file.getPath());
                    localFiles.add(localFile);
                }
            }
            return localFiles;
        }
        return null;
    }

}
