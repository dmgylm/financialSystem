package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class ResourceResult extends ResultUtils{
    @ApiModelProperty(value = "功能权限信息")
    private ResourceInfo data;

    public ResourceInfo getData() {
        return data;
    }

    public void setData(ResourceInfo data) {
        this.data = data;
    }
    
    
}
