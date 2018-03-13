package cn.financial.model;

import java.util.Date;

public class Role {
    private String id;//角色id
    private String roleName;//角色名称
    private Integer status;//删除状态0表示删除1表示存在
    private Date createTime;//创建时间
    private Date updateTime;//修改时间
 
    public Role() {
        super();
    }
    
    public Role(String id, String roleName, Date createTime, Date updateTime) {
        super();
        this.id = id;
        this.roleName = roleName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
