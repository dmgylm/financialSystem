package cn.financial.model.response;


import cn.financial.model.Resource;
import io.swagger.annotations.ApiModelProperty;

public class RsourceInfo extends ResultUtils{
    @ApiModelProperty(value = "功能权限信息对象")
    private Resource resourceById;

    public Resource getResourceById() {
        return resourceById;
    }

    public void setResourceById(Resource resourceById) {
        this.resourceById = resourceById;
    }
    
}
