package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import cn.financial.model.Organization;

public class OrganizaParnode extends ResultUtils{
	@ApiModelProperty(value = "查看配置组织结构信息")
	private List<Organization> data;

	public List<Organization> getData() {
		return data;
	}

	public void setData(List<Organization> data) {
		this.data = data;
	}
}
