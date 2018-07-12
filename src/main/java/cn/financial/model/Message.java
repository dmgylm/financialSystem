package cn.financial.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 消息实体类
 * 
 * @author zlf 2018/03/13
 *
 */
@ApiModel(value="Message对象",description="消息中心对象")
public class Message {
	
	public static final Integer PAGESIZE = 100000000;

	@ApiModelProperty(value="消息id",name="id",example="")
	String id; // 消息id(uoId)

	@ApiModelProperty(value="消息状态(0未读;1已读)",name="status",example="")
	Integer status; // 消息状态(0未读；1已读)

	@ApiModelProperty(value="消息主题(1系统提醒)",name="theme",example="")
	Integer theme; // 消息主题(1系统提醒)

	@ApiModelProperty(value="消息内容",name="content",example="")
	String content; // 消息内容

	@ApiModelProperty(value="组织接收信息",name="oId",example="")
	String oId; // 组织接收信息
    
	@ApiModelProperty(value="消息来源(这里指向发送用户的name)",name="sName",example="")
	String sName;//消息来源(这里指向发送用户的name)

	@ApiModelProperty(value="是否标注(0未标注;1标注)",name="isTag",example="")
	Integer isTag; // 是否标注(0未标注;1标注)
    
	@ApiModelProperty(value="创建时间",name="createTime",example="")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    Date createTime; // 创建时间
    
	@ApiModelProperty(value="修改时间",name="updateTime",example="")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    Date updateTime; // 修改时间
    
	@ApiModelProperty(value="用户接收信息",name="uId",example="")
	String uId;//用户接收信息
 
	@ApiModelProperty(value="保存文件路径",name="fileurl",example="")
	String fileurl;//保存文件路径
    
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

	public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	@Override
    public String toString() {
        return "Message [id=" + id + ", status=" + status + ", theme=" + theme + ", content=" + content + ", oId="
                + oId + ",sName="+ sName +", isTag=" + isTag + ", createTime=" + createTime + ", updateTime=" + updateTime + ",uId="+uId+",fileurl="+fileurl+"]";
    }

}
