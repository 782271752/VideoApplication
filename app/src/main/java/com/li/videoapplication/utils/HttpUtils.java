package com.li.videoapplication.utils;

import android.content.Context;
import android.util.Log;

import com.li.videoapplication.activity.ExApplication;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.List;

/**
 * Created by li on 2014/9/22.
 */
public class HttpUtils {

//    public static final String IMG_URL="http://121.41.128.6";
//    public static final String BASE_URL="http://121.41.128.6/home";

    public static final String IMG_URL="http://114.215.152.100";
    public static final String BASE_URL="http://114.215.152.100/home";
    /**
     * 自动获取
     * @param id
     * @return
     */
    public static String getAutoGift(String id){
        return BASE_URL+"/member/getMyPackageInfo.html?id="+id+"&member_id="+ExApplication.MEMBER_ID;
    }
;
    public static String getMyGift(String page){
        return BASE_URL+"/member/getMyPackageList.html?member_id="+ExApplication.MEMBER_ID+"&page="+page;
    }

    /**
     * 刷新token
     * @return
     */
    public static final String refreshToken(){
        return "https://openapi.youku.com/v2/oauth2/token";
    }

    /**
     *取消收藏
     * @param ids
     * @return
     */
    public static String cancelCollect(String ids){
        return BASE_URL+"/video/cancelCollect.html";
    }

    /**
     * 完成任务
     * @param id
     * @return
     */
    public static String completeMission(String id){
        return BASE_URL+"/member/finishTask.html?id="+id+"&member_id="+ExApplication.MEMBER_ID;
    }

    /**
     * 上传视频信息
     * @return
     */
    public static final String postVideoInfo(){
        return BASE_URL+"/video/publishVideo.html";
    }

    /**
     *单条视频基本信息
     * @return
     */
    public static final String youkuVideoInfo(String id){
        return "https://openapi.youku.com/v2/videos/show_basic.json?client_id="+ExApplication.id+"&video_id="+id;
    }

    /**
     * 第三方登陆
     * @return
     */
    public static final String getOtherLogin(){
        return BASE_URL+"/member/login.html";
    }

    /**
     * 获取用户资料
     * @param id
     * @return
     */
    public static final String getUserDetailInfo(String id){
        return BASE_URL+"/member/detail.html?id="+id;
    }


    /**
     * 修改用户资料
     * @return
     */
    public static final String getUpdateInfo(){
        return BASE_URL+"/member/finishMemberInfo.html";
    }

    /**
     * 获取游戏
     * @return
     */
    public static final String getGame(){
        return BASE_URL+"/game/queryAllByType.html?type_id=13";
    }



    /**
     * 升级
     * @return
     */
    public static final String getUpdateUrl(){
        return BASE_URL+"/index/getVersion.html?type=1";
    }

    /**
     * 收藏视屏
     * @param member_id
     * @param id
     * @return
     */
    public static final String getCollectVideo(String member_id,String id){
        return BASE_URL+"/video/collect.html?id="+id+"&member_id="+member_id;
    }

    /**
     * 会员收藏列表
     * @param id
     * @param page
     * @return
     */
    public static final String getCollectVideoList(String id,String page){
        return BASE_URL+"/member/collectVideoList.html?member_id="+id+"&page="+page;
    }

    /**
     * 上传头像
     * @param id
     * @return
     */
    public static final String getuploadAvatar(String id){
        return BASE_URL+"/member/uploadAvatar.html?id="+id;
    }


    /**
     * 游戏类型列表
     * @return
     */
    public static final String getGameTypeList(){
        return BASE_URL+"/game/typeList.html";
    }

    /**
     * 根据提交的类型获取视频列表
     * @param page
     * @param type_id
     * @return
     */
    public static final String getRecommendList(String page,String type_id,String type){
//        return BASE_URL+"/video/recommendList.html?page="+page+"&type_id="+type_id;
        return BASE_URL+"/video/list.html?page="+page+"&sort="+type+"&type_id="+type_id;
    }

    /**
     * 视频类型列表
     * @return
     */
    public static final String getVideoTypeList(){
        return BASE_URL+"/video/typeList.html";
    }

    /**
     * 领取礼包
     * @param member_id
     * @param id
     * @return
     */
    public static final String getGiftUrl(String member_id,String id){
        return BASE_URL+"/member/claimPackage.html?member_id="+member_id+"&id="+id;
    }

    /**
     * 提交评论
     * @return
     */
    public static final String submitCommentUrl(){
        return BASE_URL+"/video/doComment.html";
    }

    /**
     * 视频下载次数统计
     * @param id
     * @return
     */
    public static final String getDownLoadUrl(String id){
        return BASE_URL+"/video/downLoad.html?id="+id;
    }

    /**
     * 视频点赞
     * @param id
     * @return
     */
    public static final String getVedioFlowerUrl(String id,String member_id){
        return BASE_URL+"/video/flower.html?id="+id+"&member_id="+member_id;
    }

    /**
     * 获取关键字
     * @return
     */
    public static final String getKeyWordUrl(){
        return BASE_URL+"/search/keyWordList.html?page=1";
    }


    /**
     * 用户登录的路径
     * @param key
     * @return
     */
    public static final String getLoginUrl(String key){
        return BASE_URL+"/member/login.html?key="+key;
    }

    /**
     * 视频搜索的地址
     * @param key
     * @param type
     * @param page
     * @return
     */
    public static final String getSearchVideoUrl(String key,String type,String page){
        return BASE_URL+"/search/searchVideo.html?name="+key+"&sort="+type+"&page="+page;
    }

    /**
     * 商务合作的路径
     * @return
     */
    public static final String getBussinessUrl(){
        return BASE_URL+"/index/business.html";
    }

    /**
     * 返回默认礼包列表
     * @param page
     * @return
     */
    public static final String getPackageList(String memerId,String page){
        if (memerId.equals("")){
            return BASE_URL+"/member/packageList.html?page="+page;
        }else{
            return BASE_URL+"/member/packageList.html?page="+page+"&member_id="+memerId;
        }
    }

    /**
     * 返回默认任务列表
     * @return
     */
    public static final String getTaskList(String page){
        if (!ExApplication.MEMBER_ID.equals("")) {
            return BASE_URL + "/member/taskList.html?page=" + page+"&member_id="+ExApplication.MEMBER_ID;
        }
        return BASE_URL + "/member/taskList.html?page=" + page;
    }


    /**
     * 搜索礼包的路径
     * @param name
     * @param page
     * @return
     */
    public static final String getSearchGiftUrl(String name,String page){
        return BASE_URL+"/search/searchPackage.html?name="+name+"&page="+page;
    }


    /**
     * 返回搜索到的任务
     * @param name
     * @param page
     * @return
     */
    public static final String getSearchMisssionUrl(String name,String page){
        return BASE_URL+"/search/searchTask.html?name="+name+"&page="+page;
    }

    /**
     * 搜索用户昵称
     * @param nickname
     * @return
     */
    public static final String getFindUser(String nickname,String page){
        return  BASE_URL+"/search/searchMember.html?name="+nickname+"&page="+page;
    }


    /**
     * 关于我们
     * @return
     */
    public static final String getAboutUsUrl(){
        return BASE_URL+"/index/aboutUs.html";
    }

    /**
     * 反馈的地址
     * @return
     */
    public static final String getFeekbackUrl(){
        return BASE_URL+"/index/feedback.html";
    }


    /**
     * 返回推荐视频的地址
     * @param page
     * @return
     */
    public static String getRecommendUrl(String page){
        return BASE_URL+"/video/recommendList.html?page="+page;
    }

    /**
     * 获取广告位的路径
     * @return
     */
    public static String getAdVedioUrl(){
        return BASE_URL+"/focuse/list.html";
    }

    /**
     *获取礼包的路径
     * @return
     */
    public static String getGiftUrl(){
        return BASE_URL+"/member/packageList.html";
    }

    /**
     * 视频详情的路径
     * @param id
     * @return
     */
    public static String getVedioUrl(String id){
        return BASE_URL+"/video/detail.html?id="+id;
    }

    /**
     * 评论列表路径
     * @param id
     * @param page
     * @return
     */
    public static String getCommentUrl(String id,String page){
        return BASE_URL+"/video/commentList.html?id="+id+"&page="+page;
    }

    /**
     * get请求
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String str = "";
        try {
            HttpGet request = new HttpGet(url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                str = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            Log.e("httpGet", e.toString());
            str="";
        }
        Log.e("HttpGet_Response",str);
        return str;
    }

    /**
     * post请求
     * @param param  提交字段
     * @param params 路径
     * @return
     */
    public static String httpPost(List<NameValuePair> param, String...params)
    {
        String rs="";
        try {
            HttpClient httpClient= new DefaultHttpClient();////生成一个http客户端对象
            HttpParams httpparams=httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpparams, 20000);//设置连接超时时间
            HttpConnectionParams.setSoTimeout(httpparams, 600000);//设置请求超时
            HttpPost post=new HttpPost(params[0]);//客户端向服务器发送请求,返回一个响应对象
            post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
            HttpResponse response =new DefaultHttpClient().execute(post);
            if(response.getStatusLine().getStatusCode()==200)
            {
                rs=EntityUtils.toString(response.getEntity());
            }

        } catch (Exception e) {
            Log.e("httpPost",e.toString());
            rs="";
        }
        Log.e("HttpPost_Response",rs);
        return rs;

    }

    /**
     * post请求
     * @param params 路径
     * @return
     */
    public static String httpPost(String name,String descripe,String time,String id,String fileUrl, String...params)
    {
        String rs="";
        try {
            HttpClient httpClient= new DefaultHttpClient();////生成一个http客户端对象
            HttpParams httpparams=httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpparams, 20000);//设置连接超时时间
            HttpConnectionParams.setSoTimeout(httpparams, 600000);//设置请求超时
            HttpPost post=new HttpPost(params[0]);//客户端向服务器发送请求,返回一个响应对象

            MultipartEntity entity = new MultipartEntity();
//            entity.addPart("data",new StringBody(content));

            if (!fileUrl.equals("")){
                File file = new File(fileUrl);
                Log.e("Img",fileUrl);
                entity.addPart("files",new FileBody(file));
            }

            entity.addPart("member_id",new StringBody(ExApplication.MEMBER_ID));
            entity.addPart("name",new StringBody(name));
            entity.addPart("url",new StringBody(id));
            entity.addPart("descriptoin",new StringBody(descripe));
            entity.addPart("time_length",new StringBody(time));
            post.setEntity(entity);
//            post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
            HttpResponse response =new DefaultHttpClient().execute(post);
            if(response.getStatusLine().getStatusCode()==200)
            {
                rs=EntityUtils.toString(response.getEntity());
            }

        } catch (Exception e) {
            Log.e("httpPost",e.toString());
            rs="";
        }
        Log.e("HttpPost_Response",rs);
        return rs;

    }


    /**
     * 上传文件
     * @param context
     * @return
     */
    public static String postImage(Context context,String httpUrl,String fileUrl){

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(httpUrl);

            MultipartEntity entity = new MultipartEntity();
//            entity.addPart("data",new StringBody(content));

            if (!fileUrl.equals("")){
                File file = new File(fileUrl);
                Log.e("headImg",fileUrl);
                entity.addPart("upFile",new FileBody(file));
            }
            postRequest.setEntity(entity);
            HttpResponse response = httpClient.execute(postRequest);
            if(response.getStatusLine().getStatusCode()==200)
            {
                String res=EntityUtils.toString(response.getEntity());
                Log.e("response",res);
//               return EncryptUtil.decrypt(EntityUtils.toString(response.getEntity()));
                return res;
            }

        }catch (Exception e){
            Log.e("response_exception",e.toString());
            return "";
        }
        return "";
    }

}
