package cn.financial.model;

import java.util.Date;

public class UserRole {
    private String id;//用户角色关联表id
    private String rId;//角色id
    private String uId;//用户id
    private Date createTime;//创建时间
    private Date updateTime;//修改时间
    
    public UserRole() {
        super();
    }
    
    public UserRole(String id, String rId, String uId, Date createTime, Date updateTime) {
        super();
        this.id = id;
        this.rId = rId;
        this.uId = uId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getrId() {
        return rId;
    }
    public void setrId(String rId) {
        this.rId = rId;
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

}
