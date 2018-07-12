package cn.financial.model.response;

import cn.financial.model.User;
import io.swagger.annotations.ApiModelProperty;

public class UserInfo extends ResultUtils{
    @ApiModelProperty(value = "用户对象信息")
    private User userById;

    public User getUserById() {
        return userById;
    }

    public void setUserById(User userById) {
        this.userById = userById;
    }
    
}
