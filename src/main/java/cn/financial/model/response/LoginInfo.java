package cn.financial.model.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class LoginInfo extends ResultUtils{
    @ApiModelProperty(value = "登录成功返回信息")
    private List<LoginResult> data;

    public List<LoginResult> getData() {
        return data;
    }

    public void setData(List<LoginResult> data) {
        this.data = data;
    }
    
}
