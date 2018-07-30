package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class RoleResourceInfo{
    @ApiModelProperty(value = "角色功能权限信息")
    private ChildrenObject roleResourceList;

    public ChildrenObject getRoleResourceList() {
        return roleResourceList;
    }

    public void setRoleResourceList(ChildrenObject roleResourceList) {
        this.roleResourceList = roleResourceList;
    }
    
}
