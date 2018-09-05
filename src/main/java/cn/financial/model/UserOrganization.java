package cn.financial.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户组织结构关联表
 * @author gs
 * 2018/3/16
 */
@ApiModel(value="UserOrganization对象",description="用户组织架构对象UserOrganization")
public class UserOrganization {
    @ApiModelProperty(value="用户角色关联表id",name="id")
    private String id;//用户角色关联表id
    @ApiModelProperty(value="组织结构id",name="oId")
    private String oId;//组织结构id
    @ApiModelProperty(value="用户id",name="uId")
    private String uId;//用户id
    @ApiModelProperty(value="真实姓名",name="realName")
    private String realName;//真实姓名
    @ApiModelProperty(value="用户名称",name="name")
    private String name;//用户名称
    @ApiModelProperty(value="工号",name="jobNumber")
    private String jobNumber;//工号
    @ApiModelProperty(value="组织机构节点的序号",name="code")
    private String code;//组织机构节点的序号
    @ApiModelProperty(value="父节点",name="parentId")
    private String parentId;//父节点
    @ApiModelProperty(value="机构名称",name="orgName")
    private String orgName;//机构名称
    @ApiModelProperty(value="历史权限记录",name="his_permission")
    private String his_permission;//保存历史权限记录
    @ApiModelProperty(value="创建时间",name="createTime")
    private String createTime; //创建时间
    @ApiModelProperty(value="更新时间",name="updateTime")
    private String updateTime;//修改时间
    
    @ApiModelProperty(value="机构节点",name="orgType")
    private String orgType;//机构节点
    
    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
 }
