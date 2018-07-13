package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class RoleResult extends ResultUtils{
    @ApiModelProperty(value = "角色信息集合")
    private RoleInfoResult data;

    public RoleInfoResult getData() {
        return data;
    }

    public void setData(RoleInfoResult data) {
        this.data = data;
    }
    
    
}
