package com.li.videoapplication.entity;

/**保存本地的视频文件
 * Created by li on 2014/10/22.
 */
public class LocalFile {


    private String hasUpload="";
    private String name="";
    private String path="";
    private String title="";
    private String content="";

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHasUpload() {
        return hasUpload;
    }

    public void setHasUpload(String hasUpload) {
        this.hasUpload = hasUpload;
    }
}
