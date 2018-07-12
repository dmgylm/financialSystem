package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class UnreadInfo extends ResultUtils {
	
	@ApiModelProperty(value = "返回未读消息条数")
	private Integer data;

	public Integer getData() {
		return data;
	}

	public void setData(Integer data) {
		this.data = data;
	}

}
