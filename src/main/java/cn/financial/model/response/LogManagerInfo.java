package cn.financial.model.response;

import java.util.List;

import cn.financial.model.LogManagement;
import io.swagger.annotations.ApiModelProperty;

public class LogManagerInfo {

	@ApiModelProperty(value = "日志列表数据")
	private List<LogManagement> LogManagements;
	
	@ApiModelProperty(value = "数据总条数")
	private long total;

	public List<LogManagement> getLogManagements() {
		return LogManagements;
	}

	public void setLogManagements(List<LogManagement> logManagements) {
		LogManagements = logManagements;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
	
	
	
}
