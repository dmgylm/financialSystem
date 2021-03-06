package cn.financial.model.response;


import java.util.List;

import cn.financial.model.Capital;
import io.swagger.annotations.ApiModelProperty;

public class CapitalResult extends ResultUtils{

	@ApiModelProperty(value = "资金流水的返回数据")
	private List<Capital> data;  //返回的数据

	@ApiModelProperty(value = "资金流水的返回数据")
    private Capital capital;  //返回的数据
	
	@ApiModelProperty(value = "资金流水的总条数")
	private Integer total; //返回的总条数

    public List<Capital> getData() {
        return data;
    }

    public void setData(List<Capital> data) {
        this.data = data;
    }

    public Capital getCapital() {
        return capital;
    }

    public void setCapital(Capital capital) {
        this.capital = capital;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
	
}
