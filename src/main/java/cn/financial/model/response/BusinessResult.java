package cn.financial.model.response;

import java.util.List;

import cn.financial.model.Business;
import io.swagger.annotations.ApiModelProperty;

public class BusinessResult extends ResultUtils{

	@ApiModelProperty(value = "损益录入中心数据")
	private List<Business> data;  //返回的数据
	
	@ApiModelProperty(value = "损益录入中心返回的总条数")
	private Integer total; //返回的总条数

    public List<Business> getData() {
        return data;
    }

    public void setData(List<Business> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
	
}
