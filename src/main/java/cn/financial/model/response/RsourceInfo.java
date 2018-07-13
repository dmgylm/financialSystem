package cn.financial.model.response;


import cn.financial.model.Resource;
import io.swagger.annotations.ApiModelProperty;

public class RsourceInfo extends ResultUtils{
    @ApiModelProperty(value = "功能权限信息对象")
    private Resource data;

    public Resource getData() {
        return data;
    }

    public void setData(Resource data) {
        this.data = data;
    }
    
    
}
