package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;
/**
 * 是否存在子节点
 * @author whg
 *
 */
public class OrganizaHason extends ResultUtils {
	@ApiModelProperty(value = "是否存在子节点")
	private Boolean data;

	public Boolean getData() {
		return data;
	}

	public void setData(Boolean data) {
		this.data = data;
	}
	

}
