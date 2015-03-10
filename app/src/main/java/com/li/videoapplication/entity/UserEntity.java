package com.li.videoapplication.entity;

/**
 * Created by li on 2014/8/18.
 */
public class UserEntity {

//    address: "测试地址"
//    avatar: "542a171f2bd3f.png"
//    degree: "0"
//    id: "21"
//    isAdmin: "0"
//    like_gametype: "0"
//    mobile: "13760048757"
//    name: "李"
//    nickname: "开发李工"
//    openid: ""
//    password: ""
//    rank: "0"
//    sex: "1"
//    time: "1412044628"


    private String imgPath="";
    /**
     * 用户昵称
     */
    private String title="";
    private String introduce="";
    private String time="";
    private String like_gametype="";
    private String isAdmin="";
    /**
     * 排名
     */
    private String grace="";
    private String id="";
    private String address="";
    private String mobile="";
    private String name="";
    private String rank="";
    private String sex=""; //1男 2女
    private String uploadVideoCount="";
    private String openId="";
    private String password="";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUploadVideoCount() {
        return uploadVideoCount;
    }

    public void setUploadVideoCount(String uploadVideoCount) {
        this.uploadVideoCount = uploadVideoCount;
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getGrace() {
        return grace;
    }

    public void setGrace(String grace) {
        this.grace = grace;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLike_gametype() {
        return like_gametype;
    }

    public void setLike_gametype(String like_gametype) {
        this.like_gametype = like_gametype;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
