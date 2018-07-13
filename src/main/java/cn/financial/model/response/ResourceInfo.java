package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class ResourceInfo {
    @ApiModelProperty(value = "功能权限信息")
    private ChildrenObject resourceList;

    public ChildrenObject getResourceList() {
        return resourceList;
    }

    public void setResourceList(ChildrenObject resourceList) {
        this.resourceList = resourceList;
    }
}
