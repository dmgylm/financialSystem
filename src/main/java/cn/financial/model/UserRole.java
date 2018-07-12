package cn.financial.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="UserRole对象",description="用户角色对象UserRole")
public class UserRole {
    @ApiModelProperty(value="用户角色关联表id",name="id")
    private String id;//用户角色关联表id
    @ApiModelProperty(value="角色id",name="rId")
    private String rId;//角色id
    @ApiModelProperty(value="用户id",name="uId")
    private String uId;//用户id
    @ApiModelProperty(value="角色名称",name="roleName")
    private String roleName;//角色名称
    @ApiModelProperty(value="真实姓名",name="realName")
    private String realName;//真实姓名
    @ApiModelProperty(value="用户名",name="name")
    private String name;//用户名
    @ApiModelProperty(value="工号",name="jobNumber")
    private String jobNumber;//工号
    @ApiModelProperty(value="创建时间",name="createTime")
    private String createTime; //创建时间
    @ApiModelProperty(value="更新时间",name="updateTime")
    private String updateTime;//修改时间
    
    public UserRole() {
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
