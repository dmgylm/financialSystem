package cn.financial.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 账户性质项目分类表model
 * @author lmn
 *
 */
@ApiModel(value="CapitalNatrue对象",description="CapitalNatrue对象")
public class CapitalNatrue {
    
         @ApiModelProperty(value=" 账户性质项目分类表id",name="id")
         private Integer id;   // 账户性质项目分类表id
         
         @ApiModelProperty(value="名字",name="name")
         private String name;  //组织架构id
    	 
         @ApiModelProperty(value="cId (1账户性质  2项目分类)",name="cId")
         private Integer cId;   // 账户性质项目分类表 cId (1账户性质  2项目分类)

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getcId() {
            return cId;
        }

        public void setcId(Integer cId) {
            this.cId = cId;
        }
         
         
}
