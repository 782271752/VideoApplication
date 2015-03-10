package com.li.videoapplication.DB;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.li.videoapplication.entity.DownloadVideo;
import com.li.videoapplication.entity.TranscribeVideo;
import com.li.videoapplication.entity.VideoEntity;

public class DBManager {

    private Context context = null;;
    private static SQLiteDatabase db = null;
    public static final String BLE_NAME="BLE";
    DataBaseHelper dbHelper;

    public DBManager(Context context){
        this.context=context;
        dbHelper = new DataBaseHelper(context, BLE_NAME);

    }


    //    public boolean addSitua(SituaEntity entity){
//        db = dbHelper.getWritableDatabase();
//        if (db!=null) {
//            Cursor cursor = db.query("situa", new String[] {"id"},
//                    "name =?",
//                    new String[] { entity.getName()}, null, null,
//                    null);
//            try {
//                if (cursor.moveToNext()) {
//                    return false;
//                }
//            }finally {
//                cursor.close();
//            }
//
//
//            ContentValues valu=new ContentValues();
//            valu.put("name", entity.getName());
//            valu.put("ischeck", "100");
//            valu.put("eids", entity.getEids());
//            valu.put("svalues",entity.getValues());
//            valu.put("locations", entity.getLocations());
//            valu.put("starttime", entity.getStartTime());
//            valu.put("state", entity.getState());
//            db.insert("situa", null, valu);
//            db.close();
//
//        }
//        return true;
//    }



    /**
     *
     * @param address
     */
    public void updateState(String address,String value){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("state", value);
        if (db!=null) {
            db.update("ble", values, "address=?",new String[]{address});
            db.close();
        }
    }



    public void closeDB(){
        db = dbHelper.getWritableDatabase();
        if (db!=null) {
            db.close();
        }
    }

    /**
     *
     * @param address
     */
    public void updateContect(String address,String value){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isconnect", value);
        Log.e("更新连接状态",value);
        if (db!=null) {
            db.update("ble", values, "address=?",new String[]{address});
            db.close();
        }
    }


    /**
     * 判断视频是否存在
     * @param id
     * @return
     */
    public boolean isDownloadVideoExist(String id){
        db = dbHelper.getWritableDatabase();
        try {
            Cursor cursor = db.query("video", new String[]{"name"}, "video_id=?", new String[]{id}, null, null, null);
            if (cursor.moveToFirst() && cursor != null) {
                return true;
            }
        }finally {
            db.close();
        }
        return false;
    }

    /**
     * 添加下载视频到数据库
     * @param video
     * @return
     */

    public boolean addDownloadView(DownloadVideo video){
        db = dbHelper.getWritableDatabase();
        try {
            Cursor cursor = db.query("video", new String[]{"name"}, "video_id=?", new String[]{video.getId()}, null, null, null);
            if (cursor.moveToFirst() && cursor != null) {
                db.delete("video", "video_id=? ",
                        new String[] { video.getId() });
            }
            ContentValues values = new ContentValues();
            values.put("video_id",video.getId());
            Log.e("addDownloadView_id",video.getId());
            values.put("name", video.getName());
            values.put("imgUrl", video.getImgUrl());
            values.put("playUrl", video.getPlayUrl());
            db.insert("video", null, values);

            return true;
        }finally {
            db.close();
        }
    }

    /**
     * 添加录制视频
     * @param video
     * @return
     */
    public boolean addTranscribeVideo(TranscribeVideo video){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", video.getName());
        values.put("path", video.getPath());
        values.put("time",video.getTime());
        db.insert("transcribe", null, values);
        db.close();
        return true;
    }

    /**
     * 删除录制的视频
     * @return
     */
    public void delectTranscribeView(List<TranscribeVideo> list){
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            for (int i=0;i<list.size();i++){
                db.delete("transcribe", "name=? ",
                        new String[] { list.get(i).getName() });
            }
            db.close();
        }
    }


    /**
     * 删除视频
     */
    public void delectDownloadVideo(List<DownloadVideo> list){
        db = dbHelper.getWritableDatabase();
        if (db != null) {
            for (int i=0;i<list.size();i++){
                db.delete("video", "playUrl=? ",
                        new String[] { list.get(i).getPlayUrl() });
            }
            db.close();
        }
    }

    /**
     * 删除所有的数据
     */
    public void delectAllVideo(){
        db=dbHelper.getWritableDatabase();

        if (db!=null){
            List<DownloadVideo> list=new ArrayList<DownloadVideo>();
            Cursor cursor=db.query("video",null, null, null, null, null, null);

                while (cursor.moveToNext()) {
                    DownloadVideo video = new DownloadVideo();

                    video.setName(cursor.getString(cursor.getColumnIndex("name")));
                    video.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
                    video.setPlayUrl(cursor.getString(cursor.getColumnIndex("playUrl")));
                    list.add(video);
                }

            for (int i=0;i<list.size();i++){
                db.delete("video", "playUrl=? ", new String[] { list.get(i).getPlayUrl() });
            }
            db.close();
        }
    }

    /**
     * 返回录制的视频列表
     * @return
     */
    public List<TranscribeVideo> getTranscribeVideo(){
        List<TranscribeVideo> list=new ArrayList<TranscribeVideo>();
        db=dbHelper.getWritableDatabase();
        if (db!=null){
            Cursor cursor=db.query("transcribe",null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                TranscribeVideo video=new TranscribeVideo();
                video.setName(cursor.getString(cursor.getColumnIndex("name")));
                video.setPath(cursor.getString(cursor.getColumnIndex("path")));
                video.setTime(cursor.getString(cursor.getColumnIndex("time")));
                list.add(video);
            }
        }
        return list;
    }


    /**
     *获取下载的视频
     * @return
     */
    public List<DownloadVideo> getDownloadVideo(){
        List<DownloadVideo> list=new ArrayList<DownloadVideo>();
        db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("video",null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()){
                DownloadVideo video=new DownloadVideo();
                video.setId(cursor.getString(cursor.getColumnIndex("video_id")));
                video.setName(cursor.getString(cursor.getColumnIndex("name")));
                video.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
                video.setPlayUrl(cursor.getString(cursor.getColumnIndex("playUrl")));
                list.add(video);
            }
        }catch (Exception e){
            return null;
        }finally {
            cursor.close();
            db.close();
        }
        return list;
    }

    /**
     * 删除观看的视频记录
     * @return
     */
    public boolean delRecordVideo(List<VideoEntity> list){
        db = dbHelper.getWritableDatabase();
        if (db!=null) {
            //如果存在，即删除
            try {
                for (int i = 0; i < list.size(); i++) {
                    Cursor cursor = db.query("record", new String[]{"name"}, "video_id=?", new String[]{list.get(i).getId()}, null, null, null);

                    if (cursor.moveToFirst() && cursor != null) {
                        db.delete("record", "video_id=? ",
                                new String[]{list.get(i).getId()});
                    }
                }

            } finally {
                db.close();
            }
            return true;
        }
        return false;
    }



    /**
     * 添加观看记录
     * @param video
     * @return
     */
    public boolean addRecordVideo(VideoEntity video){

        db = dbHelper.getWritableDatabase();
        if (db!=null) {
            //如果存在，即删除

            try {
                Cursor cursor=db.query("record",new String[]{"name"},"video_id=?",new String[]{video.getId()},null,null,null);
                if (cursor.moveToFirst() && cursor != null) {
                    db.delete("record", "video_id=? ",
                    new String[] { video.getId() });
                }
                ContentValues values = new ContentValues();
                values.put("video_id", video.getId());
                values.put("name", video.getTitle_content());
                values.put("flag",video.getSimg_url());
                values.put("time",video.getTime());
                values.put("flower",video.getFlower());
                values.put("comment",video.getComment());
                values.put("view",video.getViewCount());

                db.insert("record", null, values);
            }finally {
                db.close();
            }
        }
        return true;
    }

    /**
     * 获取观看过的视频
     * @return
     */
    public List<VideoEntity> getRecordVideo(){
        List<VideoEntity> list=new ArrayList<VideoEntity>();
        db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("record",null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()){
                VideoEntity video=new VideoEntity();
                video.setId(cursor.getString(cursor.getColumnIndex("video_id")));
                video.setTitle_content(cursor.getString(cursor.getColumnIndex("name")));
                video.setSimg_url(cursor.getString(cursor.getColumnIndex("flag")));
                video.setFlower(cursor.getString(cursor.getColumnIndex("flower")));
                video.setComment(cursor.getString(cursor.getColumnIndex("comment")));
                video.setViewCount(cursor.getString(cursor.getColumnIndex("view")));
                video.setTime(cursor.getString(cursor.getColumnIndex("time")));
                list.add(video);
            }
        }catch (Exception e){
            return null;
        }finally {
            cursor.close();
            db.close();
        }
        return list;
    }


//
//    /**
//     * 获取模块的信息
//     * @return
//     */
//    public List<BleEntity> getBle(){
//        List<BleEntity> bleList=new ArrayList<BleEntity>();
//        db = dbHelper.getWritableDatabase();
//        Cursor cursor=db.query("ble", null, null, null, null, null, null);
//        try{
//            while (cursor.moveToNext()) {
//                BleEntity bleEntity=new BleEntity();
//                bleEntity.setAddress(cursor.getString(cursor.getColumnIndex("address")));
//                bleEntity.setState(cursor.getInt(cursor.getColumnIndex("state"))+"");
//                bleEntity.setBid(cursor.getInt(cursor.getColumnIndex("bid"))+"");
//                bleEntity.setLocation(cursor.getString(cursor.getColumnIndex("name")));
//                bleEntity.setIsconnect(cursor.getString(cursor.getColumnIndex("isconnect")));
//                bleList.add(bleEntity);
//
//            }
//        }finally{
//            cursor.close();
//            db.close();
//        }
//        return bleList;
//
//    }
//
//
//
//
//
//    /**
//     * 获取某个房间的设备
//     * @param location
//     * @return
//     */
//    public String[] getEquipmentNameByLocation(String location) {
//        String[] equipmentName = null;
//        db = dbHelper.getWritableDatabase();
//        if (db != null) {
//            Cursor cursor = db.query("equipment", new String[] { "name" }, "bid = ?",
//                    new String[] { location }, null, null, null);
//            try{
//                equipmentName = new String[cursor.getCount()];
//                for (int i = 0; cursor.moveToNext(); i++) {
//                    equipmentName[i] = cursor.getString(cursor.getColumnIndex("name"));
//                }
//            }finally{
//                cursor.close();
//                db.close();
//            }
//
//        }
//        return equipmentName;
//    }
//
//
//


//    /**
//     * 添加定时设备
//     * @param equipmentTimer
//     * @return
//     */
//    public Boolean addEquipmentTimer(TimerEntity equipmentTimer) {
//
//        db = dbHelper.getWritableDatabase();
//        if (db != null) {
//            Cursor cursor = db.query("timer", new String[] { "eid", "starttime", "state","location","tvalues"},
//                    "eid=? and starttime=? and state=? and location = ? and tvalues =? and isclosed =?",
//                    new String[] { equipmentTimer.getEid(), equipmentTimer.getStartTime(),
//                            equipmentTimer.getState(), equipmentTimer.getLocation() ,equipmentTimer.getValuse(),
//                            equipmentTimer.getIsClosed()}, null, null,
//                    null);
//            try {
//                if (cursor.moveToNext()) {
//                    return false;
//                }
//            } finally{
//                cursor.close();
//
//            }
//
//            ContentValues values = new ContentValues();
//            values.put("eid", equipmentTimer.getEid());
//            values.put("starttime", equipmentTimer.getStartTime());
//            values.put("name",equipmentTimer.getName());
//            values.put("state", equipmentTimer.getState());
//            values.put("location", equipmentTimer.getLocation());
//            values.put("tvalues", equipmentTimer.getValuse());
//
//            values.put("isclosed", equipmentTimer.getIsClosed());
//            db.insert("timer", null, values);
//            db.close();
//            return true;
//        }
//        return null;
//    }
//
//
//    /**
//     * 获取定时设备
//     * @return
//     */
//    public List<TimerEntity> getEquipmentTimer() {
//        List<TimerEntity> equipmentTimers = null;
//        db = dbHelper.getWritableDatabase();
//        if (db != null) {
//            Cursor cursor = db.query("timer", new String[] { "id", "eid", "name", "starttime",
//                    "state", "location","tvalues","isclosed"}, null, null, null, null, "id  desc");
//            equipmentTimers = new ArrayList<TimerEntity>();
//            try{
//                while(cursor.moveToNext()){
//                    TimerEntity equipmentTimer = new TimerEntity();
//                    equipmentTimer.setId(cursor.getString(cursor.getColumnIndex("id")));
//                    equipmentTimer.setEid(cursor.getString(cursor.getColumnIndex("eid")));
//                    equipmentTimer.setName(cursor.getString(cursor.getColumnIndex("name")));
//                    equipmentTimer.setStartTime(cursor.getString(cursor.getColumnIndex("starttime")));
//                    equipmentTimer.setState(cursor.getString(cursor.getColumnIndex("state")));
//                    equipmentTimer.setLocation(cursor.getString(cursor.getColumnIndex("location")));
//                    equipmentTimer.setIsClosed(cursor.getString(cursor.getColumnIndex("isclosed")));
//                    equipmentTimer.setValuse(cursor.getString(cursor.getColumnIndex("tvalues")));
//                    equipmentTimers.add(equipmentTimer);
//                }
//            }finally{
//                cursor.close();
//                db.close();
//            }
//
//        }
//        return equipmentTimers;
//    }


//
//    /**
//     * 删除定时设备记录
//     * @param equipmentTimer
//     */
//    public void deleteEquipmentTimer(String id) {
//        db = dbHelper.getWritableDatabase();
//        if (db != null) {
//            db.delete("timer", "id=? ",
//                    new String[] { id });
//            db.close();
//        }
//    }
//
//
//
//    /**
//     * 更新情景模式
//     * @param id
//     * @return
//     */
//    public boolean updateSitua(String id,String ischeck){
//        boolean success=false;
//        db = dbHelper.getWritableDatabase();
//        if (db!=null) {
//            ContentValues values = new ContentValues();
//            values.put("ischeck", ischeck);
//            db.update("situa", values, "id=?",new String[]{id});
//            success=true;
//            db.close();
//        }
//        return success;
//    }
//

//
//    /**
//     * 删除情景模式
//     * @param id
//     * @return
//     */
//    public void delSitua(String id){
//        db = dbHelper.getWritableDatabase();
//        if (db!=null) {
//            db.delete("situa", "id=? ",
//                    new String[]{id});
//            db.close();
//        }
//    }







    /**
     * 获取某个设备的位置
     * @param eid
     * @return
     */
    public String getLocation(String eid){
        String location="";
        db = dbHelper.getWritableDatabase();
        if (db!=null) {
            Cursor cursor=db.query("equipment",new String[]{"bid"},"eid=?",new String[]{eid},null,null,null);
            try {
                if (cursor.moveToFirst()&&cursor!=null) {
                    location=cursor.getString(cursor.getColumnIndex("bid"));
                    location=getLocationName(location);
                }
            } finally {
                cursor.close();
                db.close();
            }

        }
        return location;
    }


    public String getLocationId(String eid){
        String location="";
        db = dbHelper.getWritableDatabase();
        if (db!=null) {
            Cursor cursor=db.query("equipment",new String[]{"bid"},"eid=?",new String[]{eid},null,null,null);
            try {
                if (cursor.moveToFirst()&&cursor!=null) {
                    location=cursor.getString(cursor.getColumnIndex("bid"));
                }
            } finally {
                cursor.close();
                db.close();
            }


        }
        return location;
    }

    /**
     * 获取某个设备的名称
     * @param location
     * @return
     */
    public String getLocationName(String location){
        String locationName="";
        db = dbHelper.getWritableDatabase();
        if (db!=null) {
            Cursor cursor=db.query("ble",new String[]{"name"},"bid=?",new String[]{location},null,null,null);
            try {
                if (cursor.moveToFirst()&&cursor!=null) {
                    locationName=cursor.getString(cursor.getColumnIndex("name"));
                }
            } finally {
                cursor.close();
                db.close();
            }

        }
        return locationName;
    }
}
