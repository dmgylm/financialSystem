package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class ResultUtils {

	@ApiModelProperty(value = "返回状态码")
	private String resultCode;
	
	@ApiModelProperty(value = "返回状态详情")
	private String resultDesc;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	
	
	
}
