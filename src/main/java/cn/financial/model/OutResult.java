package cn.financial.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class OutResult<T> implements Serializable{

	@ApiModelProperty(value = "数据", example = "")
    public T data;

    @ApiModelProperty(value = "状态码,0表示成功 其他表示失败", example = "0")
    public int status;

    @ApiModelProperty(value = "错误信息", example = "操作成功")
    public String message = "";

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
