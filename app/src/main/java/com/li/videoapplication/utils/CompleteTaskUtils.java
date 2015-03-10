package com.li.videoapplication.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

/**完成任务
 * Created by li on 2014/11/4.
 */
public class CompleteTaskUtils {

    private String id="";
    private Context context;

    public CompleteTaskUtils(Context context,String id){
        this.id=id;
        this.context=context;
    }

    public void completeMission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            new CompleteTaskAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new CompleteTaskAsync().execute();
        }
    }


    public class CompleteTaskAsync extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            return JsonHelper.CompleteMission(id);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                ToastUtils.showToast(context,"任务完成");
            }
        }
    }




}
