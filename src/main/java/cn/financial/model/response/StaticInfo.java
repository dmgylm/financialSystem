package cn.financial.model.response;

import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class StaticInfo extends ResultUtils {

	@ApiModelProperty(value = "返回所选key对应具体公司数据")
	private Map<String, String> data;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}


}
