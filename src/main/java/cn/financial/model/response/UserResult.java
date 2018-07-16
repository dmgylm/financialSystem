package cn.financial.model.response;

import java.util.List;

import cn.financial.model.User;
import io.swagger.annotations.ApiModelProperty;

public class UserResult {
    @ApiModelProperty(value = "用户信息")
    private List<User> userList;
    @ApiModelProperty(value = "总条数")
    private Integer total;
    public List<User> getUserList() {
        return userList;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    
}
