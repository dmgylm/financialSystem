package cn.financial.model.response;

import cn.financial.model.Role;
import io.swagger.annotations.ApiModelProperty;

public class RoleInfo extends ResultUtils{
    @ApiModelProperty(value = "角色信息对象")
    private Role roleById;

    public Role getRoleById() {
        return roleById;
    }

    public void setRoleById(Role roleById) {
        this.roleById = roleById;
    }
    
}
