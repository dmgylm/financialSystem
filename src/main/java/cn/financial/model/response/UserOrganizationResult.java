package cn.financial.model.response;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;

public class UserOrganizationResult extends ResultUtils{
    @ApiModelProperty(value = "用户组织架构信息集合")
    private List<JSONObject> userOrganizationList;

    public List<JSONObject> getUserOrganizationList() {
        return userOrganizationList;
    }

    public void setUserOrganizationList(List<JSONObject> userOrganizationList) {
        this.userOrganizationList = userOrganizationList;
    }
    
}
