package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class UserResetPwdInfo extends ResultUtils{
    @ApiModelProperty(value = "重置密码列表数据")
    private UserResetPwd data;

    public UserResetPwd getData() {
        return data;
    }

    public void setData(UserResetPwd data) {
        this.data = data;
    }
}
