package com.li.videoapplication.entity;

import java.io.Serializable;

public class VideoEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private String isCheck="false";

    private String id="";
	private String title="";
	private String title_content="";
	private String time="";
	private String simg_url="";
	private String bimg_url="";
	private String all_content="";
    private String viewCount="";
    private String flower="";
    private String comment="";
    private String playUrl="";

    public String getViewCount() {
        return viewCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle_content() {
		return title_content;
	}
	public void setTitle_content(String title_content) {
		this.title_content = title_content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSimg_url() {
		return simg_url;
	}
	public void setSimg_url(String simg_url) {
		this.simg_url = simg_url;
	}
	public String getBimg_url() {
		return bimg_url;
	}
	public void setBimg_url(String bimg_url) {
		this.bimg_url = bimg_url;
	}
	public String getAll_content() {
		return all_content;
	}
	public void setAll_content(String all_content) {
		this.all_content = all_content;
	}


    public String getFlower() {
        return flower;
    }

    public void setFlower(String flower) {
        this.flower = flower;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }
}
