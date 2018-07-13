package cn.financial.model.response;


import io.swagger.annotations.ApiModelProperty;

public class HtmlResult extends ResultUtils{

	@ApiModelProperty(value = "损益录入中心编辑数据 返回html形式")
	private String data;  //返回的数据

	@ApiModelProperty(value = "损益录入中心编辑时候返回给前端的提示")
	private String mess; //返回的消息

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
}
