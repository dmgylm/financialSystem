package cn.financial.model.response;

import java.util.List;

import cn.financial.model.OrganizationMove;
import io.swagger.annotations.ApiModelProperty;

public class ListOrgMove extends ResultUtils{
	
	@ApiModelProperty(value = "返回组织架构移动记录集合")
	private List<OrganizationMove> data;

	public List<OrganizationMove> getData() {
		return data;
	}

	public void setData(List<OrganizationMove> data) {
		this.data = data;
	}

}
