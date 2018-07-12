package cn.financial.model.response;

import java.util.List;

import cn.financial.model.Organization;
import io.swagger.annotations.ApiModelProperty;
/**
 *  查询组织结构信息
 * @author whg
 *
 */
public class OganizationNode extends ResultUtils {
	
	@ApiModelProperty(value = "查询组织结构信息")
	private List<Organization> data;

	public List<Organization> getData() {
		return data;
	}

	public void setData(List<Organization> data) {
		this.data = data;
	}

	
}
