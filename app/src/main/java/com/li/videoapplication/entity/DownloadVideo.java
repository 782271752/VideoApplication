package com.li.videoapplication.entity;

/**下载本地的视频
 * Created by li on 2014/10/13.
 */
public class DownloadVideo {

    /**
     * 判断图片是否被选中
     */
    private String isCheck="false";

    private String id="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 名称
     */
    private String name="";
    /**
     * 图片路径
     */
    private String imgUrl="";

    /**
     * 存放在本地的路径
     */
    private String playUrl="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
