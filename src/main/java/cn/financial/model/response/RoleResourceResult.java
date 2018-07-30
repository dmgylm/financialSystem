package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class RoleResourceResult extends ResultUtils{
    @ApiModelProperty(value = "角色功能权限信息集合")
    private RoleResourceInfo data;

    public RoleResourceInfo getData() {
        return data;
    }

    public void setData(RoleResourceInfo data) {
        this.data = data;
    }
}
