package cn.financial.model.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class UserOrganizationInfo {
    @ApiModelProperty(value = "用户组织架构信息集合")
    private List<ChildrenObject> userOrganizationList;

    public List<ChildrenObject> getUserOrganizationList() {
        return userOrganizationList;
    }

    public void setUserOrganizationList(List<ChildrenObject> userOrganizationList) {
        this.userOrganizationList = userOrganizationList;
    }
    
}
