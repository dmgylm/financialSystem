package cn.financial.model.response;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModelProperty;

public class OrangizaSubnode extends ResultUtils{
	 @ApiModelProperty(value = "查看配置组织结构信息")
	 private JSONObject data;

	 public JSONObject getData() {
		return data;
	 }

	 public void setData(JSONObject data) {
		this.data = data;
	 }

	
	 
	 
}
