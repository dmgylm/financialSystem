package cn.financial.model.response;

import java.util.List;
import io.swagger.annotations.ApiModelProperty;
public class BusinessDataYearResult extends ResultUtils{

	@ApiModelProperty(value="年份",name="year")
	private List<Integer> year;  //返回的数据

    public List<Integer> getYear() {
        return year;
    }

    public void setYear(List<Integer> year) {
        this.year = year;
    }
	
	
    
}
