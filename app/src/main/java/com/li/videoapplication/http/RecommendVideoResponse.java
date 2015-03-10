package com.li.videoapplication.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/9/22.
 */
public class RecommendVideoResponse extends ResponseBase{


    public static class RecommendVideo{
        private String id="";
        private String title="";
        private String flag="";
        private String view_count="";
        private String flower_count="";
        private String time_length="";
        private String flagPath="";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getFlower_count() {
            return flower_count;
        }

        public void setFlower_count(String flower_count) {
            this.flower_count = flower_count;
        }

        public String getTime_length() {
            return time_length;
        }

        public void setTime_length(String time_length) {
            this.time_length = time_length;
        }

        public String getFlagPath() {
            return flagPath;
        }

        public void setFlagPath(String flagPath) {
            this.flagPath = flagPath;
        }



    }

    /**
     * 得到返回的推荐视频
     * @return
     */
    public List<RecommendVideo> getRecommendVedioList(){
        List<RecommendVideo> list=new ArrayList<RecommendVideo>();
        try{
            if (getData().equals("")){
                return null;
            }
            JSONObject object=new JSONObject(getData());
            JSONArray array=object.getJSONArray("list");
            for (int i=0;i<array.length();i++){
                RecommendVideo recommendVideo=new RecommendVideo();
                JSONObject temp=(JSONObject)array.get(i);
                recommendVideo.setId(temp.getString("id"));
                recommendVideo.setTitle(temp.getString("title"));
                recommendVideo.setFlag(temp.getString("flag"));
                recommendVideo.setView_count(temp.getString("view_count"));
                recommendVideo.setFlower_count(temp.getString("flower_count"));
                recommendVideo.setTime_length(temp.getString("time_length"));
                recommendVideo.setFlagPath(temp.getString("flagPath"));
                list.add(recommendVideo);

            }


        }catch (Exception e){

        }

        return  list;
    }




}
