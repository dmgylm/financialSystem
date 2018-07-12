package cn.financial.model.response;

import cn.financial.model.Message;
import io.swagger.annotations.ApiModelProperty;

public class MessageInfo extends ResultUtils {
	
	@ApiModelProperty(value = "返回根据id查询到的消息")
	private Message data;

	public Message getData() {
		return data;
	}

	public void setData(Message data) {
		this.data = data;
	}

}
