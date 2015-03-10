package com.li.videoapplication.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.activity.UploadVideoActivity;
import com.li.videoapplication.entity.LocalFile;
import com.li.videoapplication.entity.VedioDetail;
import com.li.videoapplication.utils.CompleteTaskUtils;
import com.li.videoapplication.utils.JsonHelper;
import com.li.videoapplication.utils.SharePreferenceUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import uploader.IUploadResponseHandler;
import uploader.YoukuUploader;

public class UploadService extends Service {


    private NotificationManager notificationManager;
    private Notification notification;
    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private int notification_id = 0;
    private  final int DOWN_OK = 1;
    private  final int DOWN_ERROR = 0;
    private YoukuUploader uploader;
    private String video_id;
    private String videoTime="";
    public UploadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        uploader = YoukuUploader.getInstance(ExApplication.id, ExApplication.client_secret, getApplicationContext());
        if (intent!=null&&intent.getExtras()!=null){

            uploadFile(ExApplication.localFileList);
            Log.e("ExApplication.localFile.Size",ExApplication.localFileList.size()+"");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    int position;
    private void uploadFile(final List<LocalFile> list){
        Log.d("path","1");
        for (int i=0;i<list.size();i++){
            createNotification();
            position=i;

            try{
                MediaPlayer mp = new MediaPlayer();
                mp.setDataSource(list.get(0).getPath());
                mp.prepare();
                videoTime=mp.getDuration()/1000+"";
                Log.e("li_test",mp.getDuration()/1000+"");
            }catch (Exception e){
                e.printStackTrace();
            }

            if (list.get(i).getHasUpload().equals("")){
                HashMap<String, String> params = new HashMap<String, String>();
//                params.put("username", "ifeimo@163.com");
//                params.put("password", "feimo1614");
                Log.e("s_token",SharePreferenceUtil.getPreference(UploadService.this,"token"));
                params.put("access_token", SharePreferenceUtil.getPreference(UploadService.this,"token"));


                HashMap<String, String> uploadInfo = new HashMap<String, String>();
                uploadInfo.put("title", list.get(i).getTitle());
                uploadInfo.put("description",list.get(i).getContent());
                uploadInfo.put("tags", "优酷,Android,原创");
                uploadInfo.put("file_name", list.get(i).getPath());
                // uploadInfo.put("file_name", "/mnt/sdcard2/download/dota2.mp4");
                Log.d("path",list.get(i).getPath());
                uploader.upload(params, uploadInfo, new IUploadResponseHandler() {

                    @Override
                    public void onStart() {
                        Log.v("Main upload", "onStart");
//                    progressBar.setProgress(0);
//                    percent.setText("等待中");
                        contentView.setTextViewText(R.id.notificationTitle, "等待中");
                        contentView.setTextViewText(R.id.notificationPercent, "0%");
                        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
                        notificationManager.notify(notification_id, notification);
                        ExApplication.UpdateState=ExApplication.UPLOADING;
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.v("Main upload onSuccess", response.toString());
                        list.get(position).setHasUpload("true");
                        handler.sendEmptyMessage(DOWN_OK);

                        try {
                            String videoId=response.getString("video_id");
                            video_id=videoId;
                            Log.e("videoId",videoId);
                            Thread.sleep(3000);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                new GetVideoInfoTask(videoId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }else{
                                new GetVideoInfoTask(videoId).execute();
                            }

                        }catch (Exception e){
                            Log.e("video_id",e.toString());
                        }


                    }

                    @Override
                    public void onProgressUpdate(int counter) {
                        Log.v("Main upload onProgress", counter + "");
//                    progressBar.setProgress(counter);
//                    percent.setText(counter + "%");
                        contentView.setTextViewText(R.id.notificationTitle, "上传中");
                        contentView.setTextViewText(R.id.notificationPercent, counter+"%");
                        contentView.setProgressBar(R.id.notificationProgress, 100, counter, false);
                        notificationManager.notify(notification_id, notification);
                    }

                    @Override
                    public void onFailure(JSONObject errorResponse) {
                        Log.v("Main upload onFailure JsonObject", errorResponse.toString());
                        handler.sendEmptyMessage(DOWN_ERROR);
                    }

                    @Override
                    public void onFinished() {
                        Log.v("Main upload", "onFinished");
//                    percent.setText("完成");

                    }
                });
            }
        }
    }




    private VedioDetail detail;
    /**
     * 获取视频信息
     */
    private class GetVideoInfoTask extends AsyncTask<Void,Void,String>{
        String id="";
        public GetVideoInfoTask(String id){
            this.id=id;
        }
        @Override
        protected String doInBackground(Void... voids) {
            detail=JsonHelper.getYouKuDetail(id);
            if (detail!=null){
                return "s";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("s")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new PostVideoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else {
                    new PostVideoTask().execute();
                }
            }
        }
    }

    /**
     * 提交视频
     */
    private class PostVideoTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            Log.e("time",videoTime+"");
            String url="";
            try{
                url=saveMyBitmap();
            }catch (Exception e){

            }

            return JsonHelper.postVideo(UploadService.this,detail.getName(),detail.getDescriptioin(), videoTime,video_id,url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:


                    Intent intent=new Intent(UploadService.this, UploadVideoActivity.class);
                    pendingIntent = PendingIntent.getActivity(
                            UploadService.this, 0, intent, 0);


                    notification.setLatestEventInfo(UploadService.this,
                            "", "上传成功", pendingIntent);

                    notificationManager.notify(notification_id, notification);
                    stopService(updateIntent);
                    ExApplication.UpdateState=ExApplication.UPLOADED;

                    CompleteTaskUtils utils;
                    utils=new CompleteTaskUtils(UploadService.this,"14");
                    utils.completeMission();

                    break;
                case DOWN_ERROR:
                    notification.setLatestEventInfo(UploadService.this,
                            "", "上传失败", null);
                    break;

                default:
                    stopService(updateIntent);
                    break;
            }

        }

    };

    /***
     * 创建通知栏
     */
    RemoteViews contentView;



    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.drawable.tubiao_top;// 这个图标必须要设置，不然下面那个RemoteViews不起作用.
        // // 这个参数是通知提示闪出来的值.
        notification.tickerText = "开始上传";

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(),
                R.layout.upload_notification_item);

        contentView.setTextViewText(R.id.notificationTitle, "正在上传");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        notification.contentView = contentView;

        updateIntent = new Intent(this, UploadVideoActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

        notification.contentIntent = pendingIntent;

        notificationManager.notify(notification_id, notification);

    }


    /**
     * 保存文件到本地
     * @return
     * @throws IOException
     */
    public String saveMyBitmap() throws IOException {
        String url=Environment.getExternalStorageDirectory() + "/yc/img/"+"video.png";
        File f = new File(url);
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExApplication.videoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return url;
    }
}
