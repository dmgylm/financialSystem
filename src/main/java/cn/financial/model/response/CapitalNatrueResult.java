package cn.financial.model.response;


import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class CapitalNatrueResult extends ResultUtils{

	@ApiModelProperty(value = "查询账户性质项目分类的返回数据")
	private Map<String, Object> data;  //返回的数据

	@ApiModelProperty(value = "返回账户性质的数据")
    private String  natrueName;  //返回的数据
	
	@ApiModelProperty(value = "返回项目分类的数据")
    private String  classifyName;  //返回的数据
	
	@ApiModelProperty(value = "查询账户性质项目分类的总条数")
	private Integer total; //返回的总条数


    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getNatrueName() {
        return natrueName;
    }

    public void setNatrueName(String natrueName) {
        this.natrueName = natrueName;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }
	
}
