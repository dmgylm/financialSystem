package cn.financial.model.response;

import java.util.List;

import cn.financial.model.BusinessDataBuild;
import io.swagger.annotations.ApiModelProperty;

public class ListBusinessDataBuild extends ResultUtils {
	
	@ApiModelProperty(value = "返回查询的记录集合")
	private List<BusinessDataBuild> data;
	
	public List<BusinessDataBuild> getData() {
		return data;
	}
	public void setData(List<BusinessDataBuild> data) {
		this.data = data;
	}

}
