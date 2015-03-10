package com.li.videoapplication.entity;

/**评论列表数据
 * Created by li on 2014/8/18.
 */
public class CommentEntity {

    private String imgPath="";
    private String name="";
    private String time="";
    private String content="";

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
