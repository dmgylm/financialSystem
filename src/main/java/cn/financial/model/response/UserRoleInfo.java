package cn.financial.model.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class UserRoleInfo {
    @ApiModelProperty(value = "用户角色对象信息")
    private List<UserChildrenObject> userRoleList;

    public List<UserChildrenObject> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserChildrenObject> userRoleList) {
        this.userRoleList = userRoleList;
    }
}
