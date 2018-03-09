package cn.financial.model;

import java.util.Date;

public class User {

    private String id; // 用户表id

    private String name; // 用户名
    
    private String realName;//真实姓名

    private String pwd; // 密码
    
    private Integer status;//状态 

    private Integer privilege; // 权限级别

    public User() {
        super();
    }

    private Date createTime; // 创建时间

    private Date updateTime; // 更新时间

    private String oId; // 组织id

    public User(String id, String name, String realName, String pwd, Integer privilege, Date createTime,
            Date updateTime, String oId) {
        super();
        this.id = id;
        this.name = name;
        this.realName = realName;
        this.pwd = pwd;
        this.privilege = privilege;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.oId = oId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
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

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

}
