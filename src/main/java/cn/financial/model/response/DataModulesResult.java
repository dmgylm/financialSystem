package cn.financial.model.response;

import java.util.List;

import cn.financial.model.DataModule;
import io.swagger.annotations.ApiModelProperty;

public class DataModulesResult extends ResultUtils{

	@ApiModelProperty(value = "配置模板列表数据")
	private List<DataModule> data;

	public List<DataModule> getData() {
		return data;
	}

	public void setData(List<DataModule> data) {
		this.data = data;
	}
	
	
}
