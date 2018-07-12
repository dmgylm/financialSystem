package cn.financial.model.response;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;

public class RoleResourceInfo extends ResultUtils{
    @ApiModelProperty(value = "角色功能权限信息")
    private JSONObject roleResourceList;

    public JSONObject getRoleResourceList() {
        return roleResourceList;
    }

    public void setRoleResourceList(JSONObject roleResourceList) {
        this.roleResourceList = roleResourceList;
    }
    
}
