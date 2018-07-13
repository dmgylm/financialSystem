package cn.financial.model.response;

import java.util.List;

import cn.financial.model.Message;
import io.swagger.annotations.ApiModelProperty;

public class ListMessage extends ResultUtils {
	
	@ApiModelProperty(value = "返回查询的消息集合")
	private List<Message> data;
	@ApiModelProperty(value = "返回总的消息条数")
	private Integer total;
	
	public List<Message> getData() {
		return data;
	}
	public void setData(List<Message> data) {
		this.data = data;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

}
