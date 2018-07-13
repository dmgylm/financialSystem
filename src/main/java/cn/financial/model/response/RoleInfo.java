package cn.financial.model.response;

import cn.financial.model.Role;
import io.swagger.annotations.ApiModelProperty;

public class RoleInfo extends ResultUtils{
    @ApiModelProperty(value = "角色信息对象")
    private Role data;

    public Role getData() {
        return data;
    }

    public void setData(Role data) {
        this.data = data;
    }
    
    
}
