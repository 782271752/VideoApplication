package com.li.videoapplication.entity;

/**录制的视频
 * Created by li on 2014/10/17.
 */
public class TranscribeVideo {
    /**
     * 判断图片是否被选中
     */
    private String isCheck="false";
    private String name="";
    private String path="";
    private String time="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }
}
