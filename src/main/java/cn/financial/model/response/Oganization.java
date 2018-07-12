package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class Oganization extends ResultUtils {
	@ApiModelProperty(value = "查询组织结构信息")
	private OrganizaList data;

	public OrganizaList getData() {
		return data;
	}

	public void setData(OrganizaList data) {
		this.data = data;
	}
}
