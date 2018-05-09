package cn.financial.model;

import java.util.Date;

/**
 * 消息实体类
 * 
 * @author zlf 2018/03/13
 *
 */
public class Message {

    String id; // 消息id(uoId)

    Integer status; // 消息状态(0未读；1已读)

    Integer theme; // 消息主题（1系统提醒）

    String content; // 消息内容

    String oId; // 组织接收信息
    
    String sName;//消息来源(这里指向发送用户的name)

    Integer isTag; // 是否标注（0未标注；1标注）

    String createTime; // 创建时间

    String updateTime; // 修改时间
    
    String uId;//用户接收信息

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

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }
    public String getsName() {
    	return sName;
    }
    
    public void setsName(String sName) {
    	this.sName = sName;
    }

    public Integer getIsTag() {
        return isTag;
    }

    public void setIsTag(Integer isTag) {
        this.isTag = isTag;
    }
    
    public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", status=" + status + ", theme=" + theme + ", content=" + content + ", oId="
                + oId + ",sName="+ sName +", isTag=" + isTag + ", createTime=" + createTime + ", updateTime=" + updateTime + ",uId="+uId+"]";
    }

}
