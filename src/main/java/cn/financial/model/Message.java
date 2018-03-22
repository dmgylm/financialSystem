package cn.financial.model;

import java.util.Date;

/**
 * 消息实体类
 * 
 * @author zlf 2018/03/13
 *
 */
public class Message {

    String id; // 消息id(uuid)

    Integer status; // 消息状态(0未读；1已读)

    Integer theme; // 消息主题（1系统提醒）

    String content; // 消息内容

    String uId; // 消息来源(这里指向接收user的id)
    
    String suId;//消息来源(这里指向发送用户的id)

    Integer isTag; // 是否标注（0未标注；1标注）

    Date createTime; // 创建时间

    Date updateTime; // 修改时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
    public String getsuId() {
    	return suId;
    }
    
    public void setsuId(String suId) {
    	this.suId = suId;
    }

    public Integer getIsTag() {
        return isTag;
    }

    public void setIsTag(Integer isTag) {
        this.isTag = isTag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", status=" + status + ", theme=" + theme + ", content=" + content + ", uId="
                + uId + ",suId="+ suId +", isTag=" + isTag + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }

}
