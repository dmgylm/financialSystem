package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class StaticJson extends ResultUtils{

	@ApiModelProperty(value = "返回html表单")
	private String data;
	@ApiModelProperty(value = "返回缓存id")
	private String caCheId;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCaCheId() {
		return caCheId;
	}

	public void setCaCheId(String caCheId) {
		this.caCheId = caCheId;
	}

}
