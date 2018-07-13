package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class UserListReslult extends ResultUtils{
    @ApiModelProperty(value = "用户信息")
    private UserResult data;

    public UserResult getData() {
        return data;
    }

    public void setData(UserResult data) {
        this.data = data;
    }
    
}
