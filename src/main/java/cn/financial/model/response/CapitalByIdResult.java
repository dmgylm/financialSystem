package cn.financial.model.response;



import cn.financial.model.Capital;
import io.swagger.annotations.ApiModelProperty;

public class CapitalByIdResult extends ResultUtils{

	@ApiModelProperty(value = "资金流水的返回数据")
	private Capital data;  //返回的数据
	
	@ApiModelProperty(value = "返回的提示信息")
	private String mess;//提示

    public Capital getData() {
        return data;
    }

    public void setData(Capital data) {
        this.data = data;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

	
}
