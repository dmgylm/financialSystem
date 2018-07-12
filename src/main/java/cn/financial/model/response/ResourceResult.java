package cn.financial.model.response;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;

public class ResourceResult extends ResultUtils{
    @ApiModelProperty(value = "功能权限信息")
    private JSONObject resourceList;

    public JSONObject getResourceList() {
        return resourceList;
    }

    public void setResourceList(JSONObject resourceList) {
        this.resourceList = resourceList;
    }
    
}
