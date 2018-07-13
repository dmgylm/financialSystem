package cn.financial.model.response;

import java.util.List;

import cn.financial.model.Role;
import io.swagger.annotations.ApiModelProperty;

public class RoleInfoResult {
    @ApiModelProperty(value = "角色信息集合")
    private List<Role> roleList;

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
