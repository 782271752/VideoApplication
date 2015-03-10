package com.li.videoapplication.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.WindowManager;

import com.fmscreenrecorder.Version;
import com.li.videoapplication.R;
import com.li.videoapplication.entity.LocalFile;
import com.li.videoapplication.entity.MissionEntity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.ryan.core.activity.AlertDialogActivity;
import com.ryan.core.activity.ExitActivity;
import com.ryan.core.config.Config;
import com.ryan.core.excatch.ExceptionHandler;
import com.ryan.core.utils.RUtils;
import com.ryan.core.utils.RedeemCodeHandler;
import com.ryan.core.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.smssdk.SMSSDK;

/**
 * Created by li on 2014/8/15.
 */
public class ExApplication{

    public static String MEMBER_ID="";
    public static String nickname="";
    public ImageLoader imageLoader = ImageLoader.getInstance();

    private static String state = Environment.getExternalStorageState();
    public static Bitmap headImg;

    /**
     * 上传状态的判断
     */
    public static int UpdateState=0;
    public final static int UPLOADING=1;
    public final static int UPLOADED=2;


    public static List<MissionEntity> missList=new ArrayList<MissionEntity>();

    /**
     * 判断点击录制的
     */
    public static int type=0;
    public final static int HOMEONCLICK=1;
    public final static int MISSIONFRAGMENTONCLICK=2;
    public static String missionId="";
    public static String timeLength="0";

    /**
     * 记录选中的上传文件
     */
    public static LocalFile localFile;
    /**
     * 记录上传
     */
    public static List<LocalFile> localFileList=new ArrayList<LocalFile>();

    /**
     * 头像文件的路径
     */
    public static String headImgUrl="";

    /**
     * 优酷
     */
    public static String accesstoken="";

    public static String id="3442d71ac40b6f05";
    public static String client_secret=" f85cf1256744b84d6c651442f2986946";
    public static String url="http://121.41.128.6/index.html";

    public static Bitmap videoBitmap;


    public ExApplication(Context context){
        initImageLoader(context);
    }

    /**
     * 创建保存图标文件夹
     */
    public static boolean creatDir() {
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/yc/img";
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return true;
        }
        return false;
    }

    /**
     * 图片展示参数
     * @return displayImageOptions
     */
    public DisplayImageOptions getOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.radio_fra_bottom_bg)
                .showImageForEmptyUri(R.drawable.radio_fra_bottom_bg)
                .showImageOnFail(R.drawable.radio_fra_bottom_bg)
                .cacheInMemory()
                .cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        return options;
    }

    /**
     * 初始化图片下载
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .enableLogging()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public void clear(){
        imageLoader.clearDiscCache();

    }


}
