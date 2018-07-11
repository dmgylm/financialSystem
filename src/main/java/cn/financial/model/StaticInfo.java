package cn.financial.model;

import io.swagger.annotations.ApiModelProperty;

public class StaticInfo extends ResultUtils {

	@ApiModelProperty(value = "返回所选key对应具体公司数据")
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
