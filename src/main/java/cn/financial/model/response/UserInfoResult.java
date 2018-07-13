package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class UserInfoResult extends ResultUtils{
    @ApiModelProperty(value = "用户对象信息")
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
    
}
