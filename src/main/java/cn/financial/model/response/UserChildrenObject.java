package cn.financial.model.response;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;

public class UserChildrenObject {
    @ApiModelProperty(value="角色id",name="id")
    private String id;//角色id
    @ApiModelProperty(value="角色名称",name="roleName")
    private String roleName;//角色名称
    @ApiModelProperty(value="删除状态0表示删除1表示存在",name="status")
    private Integer status;//删除状态0表示删除1表示存在
    @ApiModelProperty(value="创建时间",name="createTime")
    private String createTime;//创建时间
    @ApiModelProperty(value="更新时间",name="updateTime")
    private String updateTime;//修改时间
    @ApiModelProperty(value="角色功能能权限关联信息",name="jsonRoleResource")
    private UserRRChildrenObject jsonRoleResource;//角色功能能权限关联信息
    @ApiModelProperty(value="角色功能能权限关联信息匹配状态 true表示匹配false表示不匹配",name="mathc")
    private boolean mathc;//角色功能能权限关联信息匹配状态

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

    public UserRRChildrenObject getJsonRoleResource() {
        return jsonRoleResource;
    }
    
    public void setJsonRoleResource(UserRRChildrenObject jsonRoleResource) {
        this.jsonRoleResource = jsonRoleResource;
    }
    
    public boolean isMathc() {
        return mathc;
    }
    
    public void setMathc(boolean mathc) {
        this.mathc = mathc;
    }
}
