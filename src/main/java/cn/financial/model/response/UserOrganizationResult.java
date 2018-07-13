package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class UserOrganizationResult extends ResultUtils{
    @ApiModelProperty(value = "用户组织架构信息列表")
    private UserOrganizationInfo data;

    public UserOrganizationInfo getData() {
        return data;
    }

    public void setData(UserOrganizationInfo data) {
        this.data = data;
    }
    
}
