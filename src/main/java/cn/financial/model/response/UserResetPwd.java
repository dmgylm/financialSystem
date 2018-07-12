package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class UserResetPwd extends ResultUtils{
    @ApiModelProperty(value = "重置密码")
    private String resetPwd;

    public String getResetPwd() {
        return resetPwd;
    }

    public void setResetPwd(String resetPwd) {
        this.resetPwd = resetPwd;
    }
    
}
