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

    int status; // 消息状态(0未读；1已读)

    int theme; // 消息主题（0待审批；1修改申请；2审批未通过；3复核未通过；4审核通过）

    String content; // 消息内容

    String uId; // 消息来源(这里指向user的id)

    Date createTime; // 创建时间

    Date updateTime; // 修改时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
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
                + uId + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }

}
