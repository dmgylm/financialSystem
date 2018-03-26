package cn.financial.model;

import java.util.Date;
/**
 * 角色资源权限关联表
 * @author gs
 * 2018/3/15
 */
public class RoleResource {
    private String id;//角色资源权限关联表id
    private String rId;//角色id
    private String sId;//资源id
    private String roleName;//角色名称
    private String name;//资源权限名称
    private Integer code;//自增长的数字，用于选择父id
    private String url;//路径
    private String parentId;//父id
    private String permssion;//权限
    private String createTime;//创建时间
    
    public RoleResource() {
        super();
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
    public String getsId() {
        return sId;
    }
    public void setsId(String sId) {
        this.sId = sId;
    }
    
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
}
