package cn.financial.model.response;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class DataModuleResult {

	@ApiModelProperty(value = "配置模板列表数据")
	private List<ModuleList> ModuleLists;
	
	@ApiModelProperty(value = "模板列表数据总数")
	private long total;

	

	public List<ModuleList> getModuleLists() {
		return ModuleLists;
	}

	public void setModuleLists(List<ModuleList> moduleLists) {
		ModuleLists = moduleLists;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
