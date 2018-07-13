package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class UserRoleResult extends ResultUtils{
    @ApiModelProperty(value = "用户角色对象信息")
    private UserRoleInfo data;

    public UserRoleInfo getData() {
        return data;
    }

    public void setData(UserRoleInfo data) {
        this.data = data;
    }
    
    
}
