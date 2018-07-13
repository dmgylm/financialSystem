package cn.financial.model.response;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;

public class LoginResult extends ResultUtils{
    @ApiModelProperty(value = "用户功能权限信息")
    private List<JSONObject> roleResource;
    @ApiModelProperty(value = "用户组织架构信息")
    private List<JSONObject> userOrganization;
    @ApiModelProperty(value = "用户sessionId")
    private String sessionId;
    public List<JSONObject> getRoleResource() {
        return roleResource;
    }
    public void setRoleResource(List<JSONObject> roleResource) {
        this.roleResource = roleResource;
    }
    public List<JSONObject> getUserOrganization() {
        return userOrganization;
    }
    public void setUserOrganization(List<JSONObject> userOrganization) {
        this.userOrganization = userOrganization;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
}
