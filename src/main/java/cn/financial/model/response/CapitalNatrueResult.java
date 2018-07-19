package cn.financial.model.response;


import java.util.List;

import cn.financial.model.CapitalNatrue;
import io.swagger.annotations.ApiModelProperty;

public class CapitalNatrueResult extends ResultUtils{

	@ApiModelProperty(value = "查询账户性质项目分类的返回数据")
	private List<CapitalNatrue> data;  //返回的数据

	@ApiModelProperty(value = "查询账户性质项目分类的返回数据")
    private CapitalNatrue capitalNatrue;  //返回的数据
	
	@ApiModelProperty(value = "查询账户性质项目分类的总条数")
	private Integer total; //返回的总条数

    public List<CapitalNatrue> getData() {
        return data;
    }

    public void setData(List<CapitalNatrue> data) {
        this.data = data;
    }

    public CapitalNatrue getCapitalNatrue() {
        return capitalNatrue;
    }

    public void setCapitalNatrue(CapitalNatrue capitalNatrue) {
        this.capitalNatrue = capitalNatrue;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
	
}
