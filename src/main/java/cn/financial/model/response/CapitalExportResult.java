package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class CapitalExportResult extends ResultUtils{

	@ApiModelProperty(value = "资金流水导出返回的提示信息")
	private String mess;  //返回的数据

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

   
	
}
