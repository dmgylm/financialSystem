package cn.financial.model.response;

import java.util.List;

import cn.financial.model.User;
import io.swagger.annotations.ApiModelProperty;

public class UserResult extends ResultUtils{
    @ApiModelProperty(value = "用户信息")
    private List<User> userList;
    @ApiModelProperty(value = "总条数")
    private Integer userListTotal;
    public List<User> getUserList() {
        return userList;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    public Integer getUserListTotal() {
        return userListTotal;
    }
    public void setUserListTotal(Integer userListTotal) {
        this.userListTotal = userListTotal;
    }
}
