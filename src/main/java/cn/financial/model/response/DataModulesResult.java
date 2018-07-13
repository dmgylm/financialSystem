package cn.financial.model.response;


import io.swagger.annotations.ApiModelProperty;

public class DataModulesResult extends ResultUtils{

	@ApiModelProperty(value = "配置模板列表数据")
	private DataModuleResult data;

	public DataModuleResult getData() {
		return data;
	}

	public void setData(DataModuleResult data) {
		this.data = data;
	}
	

	
	
	
	
}
