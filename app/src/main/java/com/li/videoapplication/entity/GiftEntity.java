package com.li.videoapplication.entity;

import com.li.videoapplication.utils.HttpUtils;

import java.io.Serializable;

/**礼包
 * Created by li on 2014/8/16.
 */
public class GiftEntity implements Serializable{


    private String hasTake="false";

    private String id="";
    private String trade_type="";
    private String starttime="";
    private String endtime="";
    private String addtime="";
    private String imgPath="";
    private String title="";
    private String count="";
    private String introduce="";
    private String content="";
    private String activity_code="";


    public String getHasTake() {
        return hasTake;
    }

    public void setHasTake(String hasTake) {
        this.hasTake = hasTake;
    }

    public String getActivity_code() {
        return activity_code;
    }

    public void setActivity_code(String activity_code) {
        this.activity_code = activity_code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
