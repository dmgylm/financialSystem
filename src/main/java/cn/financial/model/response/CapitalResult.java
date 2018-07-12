package cn.financial.model.response;


import java.util.List;

import cn.financial.model.Capital;
import io.swagger.annotations.ApiModelProperty;

public class CapitalResult extends ResultUtils{

	@ApiModelProperty(value = "资金流水的返回数据")
	private List<Capital> capital;  //返回的数据

	@ApiModelProperty(value = "资金流水的总条数")
	private Integer total; //返回的消息

    public List<Capital> getCapital() {
        return capital;
    }

    public void setCapital(List<Capital> capital) {
        this.capital = capital;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
	
}
