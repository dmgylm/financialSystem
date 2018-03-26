package cn.financial.model;

import java.util.Date;
/**
 * 用户组织结构关联表
 * @author gs
 * 2018/3/16
 */
public class UserOrganization {
    
    private String id;//用户角色关联表id
    private String oId;//组织结构id
    private String uId;//用户id
    private String realName;//真实姓名
    private String name;//用户名称
    private String jobNumber;//工号
    private String code;//组织机构节点的序号
    private String parentId;//父节点
    private String orgName;//机构名称
    private String his_permission;//保存历史权限记录
    private String createTime; //创建时间
    
    public UserOrganization() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
    
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getHis_permission() {
        return his_permission;
    }

    public void setHis_permission(String his_permission) {
        this.his_permission = his_permission;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
 }
