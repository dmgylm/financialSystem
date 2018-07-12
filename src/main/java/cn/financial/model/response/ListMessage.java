package cn.financial.model.response;

import java.util.List;

import cn.financial.model.Message;
import io.swagger.annotations.ApiModelProperty;

public class ListMessage extends ResultUtils {
	
	@ApiModelProperty(value = "返回查询的消息集合")
	private List<Message> data;
	@ApiModelProperty(value = "返回总的消息条数")
	private Integer allSize;
	
	public List<Message> getData() {
		return data;
	}
	public void setData(List<Message> data) {
		this.data = data;
	}
	public Integer getAllSize() {
		return allSize;
	}
	public void setAllSize(Integer allSize) {
		this.allSize = allSize;
	}

}
