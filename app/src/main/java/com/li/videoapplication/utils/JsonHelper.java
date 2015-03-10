package com.li.videoapplication.utils;

import android.content.Context;
import android.util.Log;

import com.li.videoapplication.activity.ExApplication;
import com.li.videoapplication.entity.Advertisement;
import com.li.videoapplication.entity.Business;
import com.li.videoapplication.entity.CommentEntity;
import com.li.videoapplication.entity.Game;
import com.li.videoapplication.entity.GameType;
import com.li.videoapplication.entity.GiftEntity;
import com.li.videoapplication.entity.KeyWord;
import com.li.videoapplication.entity.MissionEntity;
import com.li.videoapplication.entity.SearchVideo;
import com.li.videoapplication.entity.Update;
import com.li.videoapplication.entity.VideoType;
import com.li.videoapplication.entity.UserEntity;
import com.li.videoapplication.entity.VedioDetail;
import com.li.videoapplication.entity.VideoEntity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2014/9/23.
 */
public class JsonHelper {

    private static final String TRUE = "true";
    private static final String MSG = "msg";
    private static final String DATA = "data";
    private static final String DASTA="dasta";




    public static UserEntity getUserInfo(Context context,String key){
        UserEntity user=new UserEntity();
        String result = "";
        JSONObject dasta=new JSONObject();
        String json = HttpUtils.httpGet(HttpUtils.getLoginUrl(key));
        try{
            if (json.equals("")) {
                return null;
            }

            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            dasta=object.getJSONObject(DATA);
            if (dasta.toString().equals("false")){
                return null;
            }
            SharePreferenceUtil.setUserEntity(context,json);
            user.setAddress(dasta.getString("address"));
            user.setImgPath(dasta.getString("avatar"));
            user.setGrace(dasta.getString("degree"));
            user.setId(dasta.getString("id"));
            user.setIsAdmin(dasta.getString("isAdmin"));
            user.setLike_gametype(dasta.getString("like_gametype"));
            user.setMobile(dasta.getString("mobile"));
            user.setName(dasta.getString("name"));
            user.setTitle(dasta.getString("nickname"));
            user.setOpenId(dasta.getString("openid"));
            user.setPassword(dasta.getString("password"));
            user.setRank(dasta.getString("rank"));
            user.setSex(dasta.getString("sex"));
            user.setTime(dasta.getString("time"));
            return user;
        }catch (Exception e){
            Log.e("getUerInfo",e.toString());
        }
       return null;
    }

    /**
     * 返回首页视频推荐的信息
     *
     * @param context
     * @param page
     * @return
     */
    public static List<VideoEntity> getVedioList(Context context, String page) {
        String json = HttpUtils.httpGet(HttpUtils.getRecommendUrl(page));
        String result = "";
        String msg = "";
        String data = "";
        List<VideoEntity> list = new ArrayList<VideoEntity>();
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
//            data=object.getString(DATA);
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                VideoEntity vedio = new VideoEntity();
                JSONObject temp = (JSONObject) array.get(i);
                vedio.setId(temp.getString("id"));
                vedio.setTitle_content(temp.getString("title"));
                vedio.setSimg_url(temp.getString("flagPath"));
                vedio.setFlower(temp.getString("flower_count"));


                String aa=temp.getString("time_length");
                int b=aa.indexOf(".");
                if (b!=-1){
                    String c=aa.substring(0, b);
                    vedio.setTime(TimeUtils.secToTime(Integer.parseInt(c)));
                }else{
                    vedio.setTime(TimeUtils.secToTime(Integer.parseInt(aa)));
                }



                vedio.setComment(temp.getString("comment_count"));
                vedio.setViewCount(temp.getString("view_count"));

                list.add(vedio);
            }
        } catch (Exception e) {
            Log.e("getVedioList", e.toString());
            return null;
        }
        return list;
    }



    /**
     * 获取视频详情
     *
     * @param context
     * @param id
     * @return
     */
    public static VedioDetail getVedioDetail(Context context, String id) {
        VedioDetail detail = new VedioDetail();
        String json = HttpUtils.httpGet(HttpUtils.getVedioUrl(id));
        Log.e("VedioDetail",json);
        String result = "";
        String msg = "";
        String data = "";

        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
//            data=object.getString(DATA);
            JSONObject temp = object.getJSONObject(DATA);
            detail.setUrl(temp.getString("url"));
            detail.setId(temp.getString("id"));
            detail.setType_id(temp.getString("type_id"));
            detail.setGame_id(temp.getString("game_id"));
            detail.setMember_id(temp.getString("member_id"));
            detail.setName(temp.getString("name"));
            detail.setDescriptioin(temp.getString("description"));
            detail.setContent(temp.getString("content"));
            detail.setView_count(temp.getString("view_count"));
            detail.setFlower_count(temp.getString("flower_count"));
            detail.setFlagPath(temp.getString("flagPath"));
            detail.setCollection_count(temp.getString("collection_count"));
            detail.setComment_count(temp.getString("comment_count"));
            detail.setTime_length(TimeUtils.secToTime(Integer.parseInt(temp.getString("time_length"))));
            detail.setUpload_time(temp.getString("upload_time"));
            detail.setTime(temp.getString("time"));
            detail.setType_name(temp.getString("type_name"));
            detail.setDownload_count(temp.getString("download_count"));
            detail.setGameDownloadUrl(temp.getString("gameDownloadUrl"));
            detail.setUserName(temp.getString("userName"));
            detail.setAvatar(temp.getString("avatar"));

        } catch (Exception e) {
            Log.e("getVedioDetail", e.toString());
            return null;
        }
        return detail;

    }

    public static List<Advertisement> getAdvertiseList(Context context) {
        List<Advertisement> list = new ArrayList<Advertisement>();
        String json = HttpUtils.httpGet(HttpUtils.getAdVedioUrl());
        Log.e("getAdvertiseList", HttpUtils.getAdVedioUrl());
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONArray(DATA);
            for (int i = 0; i < array.length(); i++) {
                JSONObject temp = (JSONObject) array.get(i);
                Advertisement advertise = new Advertisement();
                advertise.setVideo_id(temp.getString("video_id"));
//                advertise.setFlagPath(temp.getString("flagPath"));
                advertise.setTitle(temp.getString("title"));
                advertise.setPosition(temp.getString("position"));
                advertise.setImg_url(temp.getString("imgPath"));
                list.add(advertise);
            }
        } catch (Exception e) {
            Log.e("getAdvertise", e.toString());
            return null;
        }
        return list;
    }

    /**
     * 获取评论列表
     * @param context
     * @param id
     * @param page
     * @return
     */
    public static List<CommentEntity> getCommentList(Context context, String id, String page) {
        List<CommentEntity> list = new ArrayList<CommentEntity>();
        String json = HttpUtils.httpGet(HttpUtils.getCommentUrl(id, page));
        Log.e("CommentUrl", HttpUtils.getCommentUrl(id,page));
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++){
                CommentEntity entity=new CommentEntity();
                JSONObject temp=(JSONObject)array.get(i);
                entity.setContent(temp.getString("content"));
                entity.setImgPath(temp.getString("avatar"));
                entity.setName(temp.getString("nickname"));
                entity.setTime(temp.getString("time"));
                list.add(entity);
            }
        }catch (Exception e){
            return null;
        }
        return list;
    }


//    public static List<GiftEntity> getGiftList(Context context){
//        List<GiftEntity> list=new ArrayList<GiftEntity>();
//        String json = HttpUtils.httpGet(HttpUtils.getAdVedioUrl());
//        Log.e("getAdvertiseList", HttpUtils.getAdVedioUrl());
//        String result = "";
//        try {
//            if (json.equals("")) {
//                return null;
//            }
//            JSONObject object = new JSONObject(json);
//            result = object.getString("result");
//            if (!result.equals(TRUE)) {
//                ToastUtils.showToast(context, object.getString(MSG));
//                return null;
//            }
//            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
//            for (int i=0;i<array.length();i++) {
//                GiftEntity entity=new GiftEntity();
//                JSONObject temp = (JSONObject) array.get(i);
//                entity.setImgPath(temp.getString("flagPath"));
//                entity.setCount(temp.getString("count"));
//                entity.setAddtime(temp.getString("addtime"));
//                entity.setStarttime(temp.getString("starttime"));
//                entity.setEndtime(temp.getString("endtime"));
//                list.add(entity);
//            }
//        }catch (Exception e){
//
//        }
//        return list;
//    }

    /**
     * 得到反馈的信息
     * @param member_id
     * @param content
     * @param email
     * @return
     */
    public static String getFeekBack(String member_id,String content,String email){
        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("member_id",member_id));
        pairs.add(new BasicNameValuePair("content",content));
        pairs.add(new BasicNameValuePair("email",email));
        String json=HttpUtils.httpPost(pairs,HttpUtils.getFeekbackUrl());
        try {
            JSONObject object=new JSONObject(json);
            String result=object.getString("result");
            Log.e("-----",object.getString("result")+"-"+object.getString("msg")+"-"+object.getString("data"));
            if (result.equals("true")){
                return "s";
            }
        }catch (Exception e){
            return "";
        }

        return "";
    }




    /**
     * 获取关于我们的数据
     * @return
     */
    public static String getAboutUs(){
        String json=HttpUtils.httpGet(HttpUtils.getAboutUsUrl());
        try{
            JSONObject object=new JSONObject(json);
            String result=object.getString("result");
            if (result.equals("true")){
                return object.getString("data");
            }

        }catch (Exception e){
            return "";
        }
        return "";
    }

    public static List<UserEntity> getSearchUserList(String nickname, String page){

        List<UserEntity> list=new ArrayList<UserEntity>();
        String json = HttpUtils.httpGet(HttpUtils.getFindUser(nickname,page));
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++) {
                UserEntity entity=new UserEntity();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setImgPath(temp.getString("avatar"));
                entity.setTitle(temp.getString("nickname"));
                entity.setName(temp.getString("NAME"));
                entity.setGrace(temp.getString("degree"));
                entity.setSex(temp.getString("sex"));
                entity.setRank(temp.getString("rank"));
                entity.setUploadVideoCount(temp.getString("uploadVideoCount"));
                entity.setAddress(temp.getString("address"));
                entity.setMobile(temp.getString("mobile"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("exception",e.toString());
            return null;
        }
        return list;
    }


    public static List<MissionEntity> getSearchMissionList(String name, String page){
        List<MissionEntity> list=new ArrayList<MissionEntity>();
        String json = HttpUtils.httpGet(HttpUtils.getSearchMisssionUrl(name, page));
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++) {
                MissionEntity entity=new MissionEntity();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setImgPath(temp.getString("flagPath"));
                entity.setTitle(temp.getString("name"));
                entity.setType_id(temp.getString("type_id"));
                entity.setGame_id(temp.getString("game_id"));
                entity.setDescription(temp.getString("description"));
                entity.setContent(temp.getString("content"));
                entity.setReward(temp.getString("reward"));
                entity.setStarttime(temp.getString("starttime"));
                entity.setEndtime(temp.getString("endtime"));
                entity.setAddtime(temp.getString("addtime"));
                entity.setTaskTypeName(temp.getString("taskTypeName"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("exception",e.toString());
            return null;
        }
        return list;
    }



    public static List<GiftEntity> getSearchGift(String name, String page){
        List<GiftEntity> list=new ArrayList<GiftEntity>();
        String json = HttpUtils.httpGet(HttpUtils.getSearchGiftUrl(name, page));
        Log.e("gift_url",HttpUtils.getSearchGiftUrl(name, page));
        Log.e("gift_json",json);
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++) {
                GiftEntity entity=new GiftEntity();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setId(temp.getString("id"));
                entity.setTitle(temp.getString("title"));
                entity.setImgPath(temp.getString("flagPath"));
                entity.setContent(temp.getString("content"));
                entity.setTrade_type(temp.getString("trade_type"));
                entity.setStarttime(temp.getString("starttime"));
                entity.setEndtime(temp.getString("endtime"));
                entity.setAddtime(temp.getString("addtime"));
                entity.setCount(temp.getString("count"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("exception",e.toString());
            return null;
        }
        return list;
    }

    /**
     * 自动获取礼包详情
     * @param id
     * @return
     */
    public static GiftEntity getAutoGift(String id){
        String json = HttpUtils.httpGet(HttpUtils.getAutoGift(id));
        String result="";
        String mess="";
        GiftEntity entity=new GiftEntity();
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject jsonObject=new JSONObject(json);
            result = jsonObject.getString("result");
            Log.e("msg", jsonObject.getString(MSG));
            JSONObject temp = jsonObject.getJSONObject(DATA);


            if (!result.equals(TRUE)) {
                return null;
            }

            entity.setId(temp.getString("id"));
            entity.setTitle(temp.getString("title"));
            entity.setImgPath(temp.getString("flagPath"));
            entity.setContent(temp.getString("content"));
            entity.setTrade_type(temp.getString("trade_type"));
            entity.setStarttime(temp.getString("starttime"));
            entity.setEndtime(temp.getString("endtime"));
            entity.setAddtime(temp.getString("addtime"));
            entity.setCount(temp.getString("count"));
            entity.setActivity_code(temp.getString("activity_code"));
        }catch (Exception e){
            Log.e("getAutoGift",e.toString());
            return  null;
        }
        return entity;
    }

    /**
     * 返回默认任务列表
     * @param name
     * @param page
     * @return
     */
    public static List<MissionEntity> getMissionList(String name, String page){
        List<MissionEntity> list=new ArrayList<MissionEntity>();
        String json = HttpUtils.httpGet(HttpUtils.getTaskList(page));
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            String msg=object.getString("msg");
            Log.e("msg",msg);
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++) {
                MissionEntity entity=new MissionEntity();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setId(temp.getString("id"));
                entity.setImgPath(temp.getString("flagPath"));
                entity.setTitle(temp.getString("name"));
                entity.setType_id(temp.getString("type_id"));
                entity.setGame_id(temp.getString("game_id"));
                entity.setDescription(temp.getString("description"));
                entity.setContent(temp.getString("content"));
                entity.setReward(temp.getString("reward"));
                entity.setStarttime(temp.getString("starttime"));
                entity.setEndtime(temp.getString("endtime"));
                entity.setAddtime(temp.getString("addtime"));
                entity.setTaskTypeName(temp.getString("type_name"));
                entity.setTaskTimeLength(temp.getString("taskTimeLength"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("exception",e.toString());
            return null;
        }
        return list;
    }


    /**
     * 获取默认礼包
     * @param name
     * @param page
     * @return
     */
    public static List<GiftEntity> getPakage(String name, String page){
        List<GiftEntity> list=new ArrayList<GiftEntity>();
        String json = HttpUtils.httpGet(HttpUtils.getPackageList(ExApplication.MEMBER_ID,page));
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++) {
                GiftEntity entity=new GiftEntity();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setId(temp.getString("id"));
                entity.setTitle(temp.getString("title"));
                entity.setImgPath(temp.getString("flagPath"));
                entity.setContent(temp.getString("content"));
                entity.setTrade_type(temp.getString("trade_type"));
                entity.setStarttime(temp.getString("starttime"));
                entity.setEndtime(temp.getString("endtime"));
                entity.setAddtime(temp.getString("addtime"));
                entity.setCount(temp.getString("count"));
                entity.setActivity_code(temp.getString("activity_code"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("exception",e.toString());
            return null;
        }
        return list;
    }

    /**
     * 获取我的礼包
     * @param name
     * @param page
     * @return
     */
    public static List<GiftEntity> getMyPakage(String name, String page){
        List<GiftEntity> list=new ArrayList<GiftEntity>();
        String json = HttpUtils.httpGet(HttpUtils.getMyGift(page));
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++) {
                GiftEntity entity=new GiftEntity();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setId(temp.getString("id"));
                entity.setTitle(temp.getString("title"));
                entity.setImgPath(temp.getString("flagPath"));
                entity.setContent(temp.getString("content"));
                entity.setTrade_type(temp.getString("trade_type"));
                entity.setStarttime(temp.getString("starttime"));
                entity.setEndtime(temp.getString("endtime"));
                entity.setAddtime(temp.getString("addtime"));
                entity.setCount(temp.getString("count"));
                entity.setActivity_code(temp.getString("activity_code"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("exception",e.toString());
            return null;
        }
        return list;
    }

    /**
     * 获取商务信息
     * @return
     */
    public static List<Business> getBusiness(){
        List<Business> list=new ArrayList<Business>();
        String json = HttpUtils.httpGet(HttpUtils.getBussinessUrl());
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONArray(DATA);
            for (int i=0;i<array.length();i++) {
                Business entity=new Business();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setId(temp.getString("id"));
                entity.setEmail(temp.getString("email"));
                entity.setPhone(temp.getString("phone"));
                entity.setAddress(temp.getString("address"));
                entity.setWebsite(temp.getString("website"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("exception",e.toString());
            return null;
        }
        return list;
    }





    /**
     * 返回搜索到的视频
     * @param name
     * @param type sort: time(按时间排序)  flower（按点赞数排序）
     * @param page
     * @return
     */
    public static List<VideoEntity> getSearchVideo(String name,String type,String page){
        List<VideoEntity> list=new ArrayList<VideoEntity>();
        String json = HttpUtils.httpGet(HttpUtils.getSearchVideoUrl(name,type,page));
        Log.e("video_url",HttpUtils.getSearchVideoUrl(name,type,page));
        Log.e("video_json",json);
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++) {
                VideoEntity vedio=new VideoEntity();
                JSONObject temp = (JSONObject) array.get(i);
////                entity.setCollection_count(temp.getString("collection_count"));
//                entity.setComment(temp.getString("comment_count"));
//                entity.setTitle_content(temp.getString("content"));
//
////                entity.setDescription(temp.getString("description"));
//                entity.setSimg_url(temp.getString("flagPath"));
//                entity.setFlower(temp.getString("flower_count"));
////                entity.setGameName(temp.getString("gameName"));
////                entity.setGame_id(temp.getString("game_id"));
//                entity.setId(temp.getString("id"));
////                entity.setMember_id(temp.getString("member_id"));
//                entity.setTitle(temp.getString("name"));
//                entity.setTime(temp.getString("time"));
//                entity.setTypeName(temp.getString("typeName"));
//                entity.setType_id(temp.getString("type_id"));
//                entity.setUpload_time(temp.getString("upload_time"));
//                entity.setUrl(temp.getString("url"));
//                entity.setView_count(temp.getString("view_count"));
                vedio.setId(temp.getString("id"));
                vedio.setTitle(temp.getString("gameName"));
                vedio.setTitle_content(temp.getString("name"));
                vedio.setSimg_url(temp.getString("flagPath"));
                vedio.setFlower(temp.getString("flower_count"));
                vedio.setTime(TimeUtils.secToTime(Integer.parseInt(temp.getString("time_length"))));
                vedio.setComment(temp.getString("comment_count"));
                vedio.setViewCount(temp.getString("view_count"));
                list.add(vedio);
            }
        }catch (Exception e){
            Log.e("exception",e.toString());
            return null;
        }
        return list;
    }

    /**
     * 获取关键字
     * @return
     */
    public static List<KeyWord> getKeyWord(){
        List<KeyWord> list=new ArrayList<KeyWord>();
        String json = HttpUtils.httpGet(HttpUtils.getKeyWordUrl());
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++) {
                KeyWord entity=new KeyWord();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setId(temp.getString("id"));
                entity.setWord(temp.getString("word"));
                entity.setSite(temp.getString("site"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("keyword_exception",e.toString());
            return null;
        }
        return list;
    }



    /**
     * 提交评论
     * @param id
     * @param userId
     * @param content
     * @return
     */
    public static boolean submitComment(String id,String userId,String content){
        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id",id));
        pairs.add(new BasicNameValuePair("member_id",userId));
        pairs.add(new BasicNameValuePair("content",content));
        String json=HttpUtils.httpPost(pairs,HttpUtils.submitCommentUrl());
        String result="";
        if (json.equals("")){
            return false;
        }
        try {
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (result.equals(TRUE)) {
                return true;
            }
        }catch (Exception e){
            Log.e("submitComment","submitComment");
            return false;
        }
        return false;
    }

    /**
     * 领取礼包
     * @param userId
     * @param id
     * @return
     */
    public static boolean getGiftResponse(String userId,String id){
        String url=HttpUtils.getGiftUrl(userId,id);
        Log.e("url",url);
        String json=HttpUtils.httpGet(url);

        String result="";
        if (json.equals("")){
            return false;
        }
        try {
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (result.equals(TRUE)) {
                return true;
            }
        }catch (Exception e){
            Log.e("submitComment","submitComment");
            return false;
        }
        return false;
    }

    /**
     * 点击下载
     * @param id
     * @return
     */
    public static boolean addDownLoadCount(String id){
        String json=HttpUtils.httpGet(HttpUtils.getDownLoadUrl(id));
        Log.e("download",json);
        String result = "";
        try {
            if (json.equals("")) {
                return false;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (result.equals(TRUE)) {
                return true;
            }
        }catch (Exception e){
            Log.e("download_exception",e.toString());
        }
        return false;
    }

    /**
     *  获取返回视频类型列表
     * @return
     */
    public static List<VideoType> getVideoType(){
        List<VideoType> list=new ArrayList<VideoType>();
        String json=HttpUtils.httpGet(HttpUtils.getVideoTypeList());
        Log.e("getVideoType",json);
        String result = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONArray(DATA);
            for (int i=0;i<array.length();i++) {
                VideoType entity = new VideoType();
                JSONObject temp = (JSONObject) array.get(i);
                entity.setId(temp.getString("id"));
                entity.setName(temp.getString("name"));
                entity.setSort(temp.getString("sort"));
                entity.setParent_id(temp.getString("parent_id"));
                entity.setPath(temp.getString("path"));
                entity.setLevel(temp.getString("level"));
                list.add(entity);
            }
        }catch (Exception e){
            Log.e("download_exception",e.toString());
            return null;
        }
        return list;
    }

    /**
     * 分类视屏
     *
     * @param context
     * @param page
     * @return
     */
    public static List<VideoEntity> getVideoTypeList(Context context, String page,String type_id,String type) {
        String json = HttpUtils.httpGet(HttpUtils.getRecommendList(page,type_id,type));
        String result = "";
        String msg = "";
        String data = "";
        List<VideoEntity> list = new ArrayList<VideoEntity>();
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
//            data=object.getString(DATA);
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                VideoEntity vedio = new VideoEntity();
                JSONObject temp = (JSONObject) array.get(i);
                vedio.setId(temp.getString("id"));
                vedio.setTitle_content(temp.getString("title"));
                vedio.setSimg_url(temp.getString("flagPath"));
                vedio.setFlower(temp.getString("flower_count"));
                vedio.setTime(TimeUtils.secToTime(Integer.parseInt(temp.getString("time_length"))));
                vedio.setComment(temp.getString("comment_count"));
                vedio.setViewCount(temp.getString("view_count"));

                list.add(vedio);
            }
        } catch (Exception e) {
            Log.e("getVedioList", e.toString());
            return null;
        }
        return list;
    }

    /**
     * 获取游戏类型
     * @return
     */
    public static List<GameType> getGameTypeList(){
        List<GameType> list=new ArrayList<GameType>();
        String json = HttpUtils.httpGet(HttpUtils.getGameTypeList());
        String result = "";
        String msg = "";
        String data = "";
        try {
            if (json.equals("")) {
                return null;
            }

            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }

            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                GameType gameType=new GameType();
                JSONObject temp = (JSONObject) array.get(i);
                gameType.setId(temp.getString("id"));
                gameType.setName(temp.getString("name"));
                gameType.setFlag(temp.getString("flag"));
                gameType.setSort(temp.getString("sort"));
                gameType.setHotrank("hotrank");
                list.add(gameType);
            }
        } catch (Exception e) {
            Log.e("getVedioList", e.toString());
            return null;
        }
        return list;
    }

    public static boolean uploadFile(Context context,String id,String url){
        String json = HttpUtils.postImage(context, HttpUtils.getuploadAvatar(id), url);
        String result = "";
        String msg = "";
        String data = "";
        try {
            if (json.equals("")) {
                return false;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            Log.e("msg",object.getString("msg"));
            if (!result.equals(TRUE)) {
                return false;
            }
        }catch (Exception e){
            Log.e("uploadFile",e.toString());
            return false;
        }
        return true;
    }

    /**
     * 点赞
     * @param id
     * @return
     */
    public static String giveflower(String id,String member_id){
        String json=HttpUtils.httpGet(HttpUtils.getVedioFlowerUrl(id,member_id));
        Log.e("flower",json);
        String result = "";
        try {
            if (json.equals("")) {
                return "";
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (result.equals(TRUE)) {
                return "s";
            }else{
                JSONObject dataobjcet = object.getJSONObject(DATA);
                if (dataobjcet.has("hadFlower")){
                    if (dataobjcet.getString("hadFlower").equals(TRUE)){
                        return "c";
                    }
                }
            }
        }catch (Exception e){
            Log.e("giveflower",e.toString());
            return "";
        }
        return "f";
    }

    /**
     * 收藏视频
     * @return
     */
    public static String getCollectVideo(String member_id,String id){
        String json = HttpUtils.httpGet(HttpUtils.getCollectVideo(member_id, id));
        String result = "";
        String msg = "";
        String data = "";
        try {
            if (json.equals("")) {
                return "";
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (result.equals(TRUE)) {
                return "s";
            }else{
                JSONObject dataobjcet = object.getJSONObject(DATA);
                if (dataobjcet.has("hadCollect")){
                    if (dataobjcet.getString("hadCollect").equals(TRUE)){
                        return "c";
                    }
                }
            }

        }catch (Exception e){
            Log.e("getCollectVideo",e.toString());
            return "";
        }
        return "f";
    }

    /**
     * 获取我的收藏列表
     * @param context
     * @param memer_id
     * @param page
     * @return
     */
    public static List<VideoEntity> getCollectList(Context context,String memer_id,String page) {
        String json = HttpUtils.httpGet(HttpUtils.getCollectVideoList(memer_id,page));
        String result = "";
        String msg = "";
        String data = "";
        List<VideoEntity> list = new ArrayList<VideoEntity>();
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                VideoEntity vedio = new VideoEntity();
                JSONObject temp = (JSONObject) array.get(i);
                vedio.setId(temp.getString("video_id"));
                vedio.setTitle_content(temp.getString("name"));
                vedio.setSimg_url(temp.getString("flagPath"));
                vedio.setFlower(temp.getString("flower_count"));
                vedio.setTime(TimeUtils.secToTime(Integer.parseInt(temp.getString("time_length"))));
                vedio.setComment(temp.getString("comment_count"));
                vedio.setViewCount(temp.getString("view_count"));

                list.add(vedio);
            }
        } catch (Exception e) {
            Log.e("getCollectList", e.toString());
            return null;
        }
        return list;
    }

    /**
     * 获取升级版本号
     * @return
     */
    public static Update getUpdate(){
        String json = HttpUtils.httpGet(HttpUtils.getUpdateUrl());
        String result = "";
        String msg = "";
        String data = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array=object.getJSONArray(DATA);
            JSONObject temp=(JSONObject)array.get(0);
            Update update=new Update();
            update.setId(temp.getString("id"));
            update.setCode(temp.getString("code"));
            update.setDownload_url(temp.getString("download_url"));
            update.setType(temp.getString("type"));
            update.setUpdate_time(temp.getString("update_time"));
            return update;

        }catch (Exception e){
            Log.e("update",e.toString());
            return null;
        }
    }



    /**
     * 获取游戏列表
     * @return
     */
    public static List<Game> getGameList(){
        List<Game> list=new ArrayList<Game>();
        String json = HttpUtils.httpGet(HttpUtils.getGame());
        String result = "";
        String msg = "";
        String data = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONArray array = object.getJSONObject(DATA).getJSONArray("list");
            for (int i=0;i<array.length();i++){
                JSONObject temp = (JSONObject) array.get(i);
                Game game=new Game();
                game.setId(temp.getString("id"));
                game.setName(temp.getString("name"));
                game.setFlagPath(temp.getString("flagPath"));
                list.add(game);
            }

        }catch (Exception e){
            Log.e("",e.toString());
            return null;
        }
        return list;
    }

    /**
     * 更新用户信息
     * @param member_id
     * @param name
     * @param mobile
     * @param nickName
     * @param sex 0女 1男
     * @param address
     * @return
     */
    public static String getUpdateInfo(String member_id,String name,String mobile,String nickName,
                                       String sex,String address,String like_gametype){
        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("id",member_id));
        if (!name.equals("")){
            pairs.add(new BasicNameValuePair("name",name));
        }
        if (!mobile.equals("")){
            pairs.add(new BasicNameValuePair("mobile",mobile));
        }
        if (!nickName.equals("")){
            pairs.add(new BasicNameValuePair("nickname",nickName));
        }
        if (!sex.equals("")){
            pairs.add(new BasicNameValuePair("sex",sex));
        }
        if (!address.equals("")){
            pairs.add(new BasicNameValuePair("address",address));
        }
        if (!like_gametype.equals("")){
            pairs.add(new BasicNameValuePair("like_gametype",like_gametype));
        }

        Log.e("post",pairs.toString());
        String json=HttpUtils.httpPost(pairs,HttpUtils.getUpdateInfo());
        try {
            JSONObject object=new JSONObject(json);
            String result=object.getString("result");
            Log.e("-----",object.getString("result")+"-"+object.getString("msg")+"-"+object.getString("data"));
            if (result.equals("true")){
                return "s";
            }
        }catch (Exception e){
            return "";
        }

        return "";
    }


    /**
     * 得到accesstoken
     * @return
     */
    public static String getAccessToken(String code){
        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("client_id",ExApplication.id));
        pairs.add(new BasicNameValuePair("client_secret",ExApplication.client_secret));
        pairs.add(new BasicNameValuePair("grant_type","authorization_code"));
        pairs.add(new BasicNameValuePair("code",code));
        pairs.add(new BasicNameValuePair("redirect_uri",ExApplication.url));
        String json=HttpUtils.httpPost(pairs,"https://openapi.youku.com/v2/oauth2/token");
        try {
            JSONObject object=new JSONObject(json);
            String result=object.getString("access_token");
            String refreshToken=object.getString("refresh_token");
//            SharePreferenceUtil.setPreference();
            Log.e("-----",result);
            return result;
        }catch (Exception e){
            return "";
        }

    }

    /**
     * 第三用户
     * @param context
     * @param openId
     * @param nickname
     * @return
     */
    public static UserEntity getOtherUser(Context context,String openId,String nickname){
        UserEntity user=new UserEntity();
        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("key",openId));
        pairs.add(new BasicNameValuePair("isOpenId","1"));
        pairs.add(new BasicNameValuePair("nickname",nickname));
        pairs.add(new BasicNameValuePair("name",nickname));
        Log.e("post",pairs.toString());
        String json=HttpUtils.httpPost(pairs,HttpUtils.getOtherLogin());
        try {
            JSONObject object=new JSONObject(json);
            String result=object.getString("result");
            String msg=object.getString("msg");
            Log.e("msg",msg);
            if (!result.equals(TRUE)){
                return null;
            }
            JSONObject data=object.getJSONObject(DATA);
//            JSONObject data=(JSONObject)list.get(0);
//            if (data.toString().equals("false")){
//                return null;
//            }
            user.setImgPath(data.getString("avatar"));
            user.setId(data.getString("id"));
            user.setName(data.getString("name"));
            user.setTitle(data.getString("nickname"));
            user.setAddress(data.getString("address"));
            user.setGrace(data.getString("degree"));
            user.setLike_gametype(data.getString("like_gametype"));
            user.setRank(data.getString("rank"));
            user.setSex(data.getString("sex"));
            SharePreferenceUtil.setUserEntity(context,json);
        }catch (Exception e){
            Log.e("getUerInfo",e.toString());
            return null;
        }
        return user;
    }



    public static UserEntity getUserDetailInfo(Context context,String id){
        UserEntity user=new UserEntity();
        String json = HttpUtils.httpGet(HttpUtils.getUserDetailInfo(id));
        Log.e("------------------------",json);
        String result = "";
        String msg = "";
//        String data = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            if (!result.equals(TRUE)) {
                return null;
            }
            JSONObject data=object.getJSONObject(DATA);
//            JSONObject data=(JSONObject)list.get(0);
//            if (data.toString().equals("false")){
//                return null;
//            }
            user.setAddress(data.getString("address"));
            user.setImgPath(data.getString("avatar"));
            user.setGrace(data.getString("degree"));
            user.setId(data.getString("id"));
            user.setIsAdmin(data.getString("isAdmin"));
            user.setLike_gametype(data.getString("like_gametype"));
            user.setMobile(data.getString("mobile"));
            user.setName(data.getString("name"));
            user.setTitle(data.getString("nickname"));
            user.setOpenId(data.getString("openid"));
            user.setPassword(data.getString("password"));
            user.setRank(data.getString("rank"));
            user.setSex(data.getString("sex"));
            user.setTime(data.getString("time"));

            SharePreferenceUtil.setUserEntity(context,json);
        }catch (Exception e){
            Log.e("getUerInfo",e.toString());
            return null;
        }
        return user;
    }

    /**
     * 获取优酷视频
     * @param id
     * @return
     */
    public static VedioDetail getYouKuDetail(String id){
        VedioDetail detail =new VedioDetail();
        String json = HttpUtils.httpGet(HttpUtils.youkuVideoInfo(id));
        Log.e("------------------------",json);
        String result = "";
        String msg = "";
//        String data = "";
        try {
            if (json.equals("")) {
                return null;
            }
            JSONObject object = new JSONObject(json);
            detail.setUrl(object.getString("player"));
            detail.setName(object.getString("title"));
            detail.setDescriptioin(object.getString("description"));
            detail.setLink(object.getString("link"));
            if (object.has("thumbnail")){
                detail.setFlagPath(object.getString("thumbnail"));
            }
            if (object.has("duration")) {
                detail.setTime(object.getString("duration"));
            }

            return detail;
        }catch (Exception e){
            Log.e("getDetail",e.toString());
            return null;
        }
    }

    /**
     * 提交视频
     * @param context
     * @param name
     * @param description
     * @param time
     * @param url
     * @param img
     * @return
     */
    public static String postVideo(Context context,String name,String description,
                                   String time,String url,String img){

//        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
//        pairs.add(new BasicNameValuePair("member_id",ExApplication.MEMBER_ID));
//        pairs.add(new BasicNameValuePair("name",name));
//        pairs.add(new BasicNameValuePair("url",url));
//        pairs.add(new BasicNameValuePair("descriptoin",description));
//        pairs.add(new BasicNameValuePair("files",img));
//        pairs.add(new BasicNameValuePair("time_length", time));
//        Log.e("postVideo",pairs.toString());
        String response=HttpUtils.httpPost(name,description,time,url,img,HttpUtils.postVideoInfo());
        try {
            JSONObject object = new JSONObject(response);
            String result = object.getString("result");
            String msg = object.getString("msg");
            Log.e("msg", msg);
            if (!result.equals(TRUE)) {
                return "";
            }
        }catch (Exception e){
            Log.e("postVideo",e.toString());
            return "";
        }
        return response;

    }

    /**
     * 完成任务
     * @param id
     * @return
     */
    public static boolean CompleteMission(String id){
        String json=HttpUtils.httpGet(HttpUtils.completeMission(id));
        Log.e("mission",json);
        String result = "";
        try {
            if (json.equals("")) {
                return false;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            String msg=object.getString("msg");
            Log.e("cmoplete_msg",msg);
            if (result.equals(TRUE)) {
                return true;
            }
        }catch (Exception e){
            Log.e("complete_mission",e.toString());
        }
        return false;
    }

    /**
     * 取消收藏
     * @param ids
     * @return
     */
    public static boolean cancelCollect(String ids){
        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("member_id",ExApplication.MEMBER_ID));
        pairs.add(new BasicNameValuePair("id",ids));
        Log.e("collect_post",pairs.toString());
        String json=HttpUtils.httpPost(pairs,HttpUtils.cancelCollect(ids));
        Log.e("cancelCollect",json);
        String result = "";
        try {
            if (json.equals("")) {
                return false;
            }
            JSONObject object = new JSONObject(json);
            result = object.getString("result");
            String msg=object.getString("msg");
            Log.e("cancelCollect_msg",msg);
            if (result.equals(TRUE)) {
                return true;
            }
        }catch (Exception e){
            Log.e("cancelCollect",e.toString());
        }
        return false;
    }

    /**
     * 刷新accesstoken
     * @param context
     * @return
     */
    public static boolean refreshToken(Context context){
        List<NameValuePair> pairs=new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("client_id",ExApplication.id));
        pairs.add(new BasicNameValuePair("client_secret",ExApplication.client_secret));
        pairs.add(new BasicNameValuePair("grant_type","refresh_token"));
        pairs.add(new BasicNameValuePair("refresh_token",SharePreferenceUtil.getPreference(context,"refresh")));
        String json=HttpUtils.httpPost(pairs,HttpUtils.refreshToken());
        Log.e("refresh",json);
        String token = "";
        String refresh="";
        try {
            if (json.equals("")) {
                return false;
            }
            JSONObject object = new JSONObject(json);
            token = object.getString("access_token");
            refresh=object.getString("refresh_token");
            SharePreferenceUtil.setPreference(context,"token",token);
            SharePreferenceUtil.setPreference(context,"refresh",refresh);
           return true;
        }catch (Exception e){
            Log.e("cancelCollect",e.toString());
            return false;
        }
    }
}


