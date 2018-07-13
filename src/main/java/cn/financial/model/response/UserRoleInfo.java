package cn.financial.model.response;

import java.util.List;

import cn.financial.model.UserRole;
import io.swagger.annotations.ApiModelProperty;

public class UserRoleInfo {
    @ApiModelProperty(value = "用户角色对象信息")
    private List<UserRole> userRoleList;

    public List<UserRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }
}
