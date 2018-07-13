package cn.financial.model.response;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;

public class OrangizaSubnode extends ResultUtils{
	 @ApiModelProperty(value = "查询所有该节点的子节点")
	 private List<OrganizaResult> data;

	public List<OrganizaResult> getData() {
	        return data;
	 }

	public void setData(List<OrganizaResult> data) {
	        this.data = data;
	  }

	
	 
	 
}
