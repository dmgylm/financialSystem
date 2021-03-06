package cn.financial.model.response;



import cn.financial.model.Capital;
import io.swagger.annotations.ApiModelProperty;

public class CapitalByIdResult extends ResultUtils{

	@ApiModelProperty(value = "资金流水的返回数据")
	private Capital data;  //返回的数据
	

    public Capital getData() {
        return data;
    }

    public void setData(Capital data) {
        this.data = data;
    }

}
