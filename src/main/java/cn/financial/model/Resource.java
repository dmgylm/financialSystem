package cn.financial.model;

import java.util.Date;
/**
 * 资源权限表
 * @author gs
 * 2018/3/15
 */
public class Resource {
    private String id;//资源权限id
    private String name;//资源权限名称
    private Integer code;//自增长的数字，用于选择父id
    private String url;//路径
    private String parentId;//父id
    private String permssion;//权限
    private Integer status;//删除状态0表示删除1表示存在
    private Date createTime;//创建时间
    private Date updateTime;//修改时间
 
    public Resource() {
        super();
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPermssion() {
        return permssion;
    }

    public void setPermssion(String permssion) {
        this.permssion = permssion;
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
