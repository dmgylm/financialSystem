package cn.financial.model.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class LoginResult{
    @ApiModelProperty(value = "用户名")
    private List<String> userName;
    @ApiModelProperty(value = "用户角色信息")
    private List<String> roleName;
    @ApiModelProperty(value = "用户功能权限信息")
    private List<ChildrenObject> roleResource;
    @ApiModelProperty(value = "用户组织架构信息")
    private List<ChildrenObject> userOrganization;
    @ApiModelProperty(value = "用户sessionId")
    private String sessionId;
    
    public List<String> getUserName() {
        return userName;
    }
    public void setUserName(List<String> userName) {
        this.userName = userName;
    }
    public List<String> getRoleName() {
        return roleName;
    }
    public void setRoleName(List<String> roleName) {
        this.roleName = roleName;
    }
    public List<ChildrenObject> getRoleResource() {
        return roleResource;
    }
    public void setRoleResource(List<ChildrenObject> roleResource) {
        this.roleResource = roleResource;
    }
    public List<ChildrenObject> getUserOrganization() {
        return userOrganization;
    }
    public void setUserOrganization(List<ChildrenObject> userOrganization) {
        this.userOrganization = userOrganization;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }   
}
