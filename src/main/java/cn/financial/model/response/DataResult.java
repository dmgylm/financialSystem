package cn.financial.model.response;



import cn.financial.model.DataModule;
import io.swagger.annotations.ApiModelProperty;

public class DataResult extends ResultUtils{

	@ApiModelProperty(value = "配置模板列表数据")
	private DataModule data;

	public DataModule getData() {
		return data;
	}

	public void setData(DataModule data) {
		this.data = data;
	}
	
	
}
