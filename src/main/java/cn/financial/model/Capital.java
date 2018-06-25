package cn.financial.model;

import java.util.Date;

/**
 * 资金表model
 * @author lmn
 *
 */
public class Capital {
	
         private String id;   //资金表id
    	 
         /*private String plate; //板块
         
         private String BU; //事业部
         
         private String regionName; //大区名称
         
         private String province; //省份
         
         private String city; //城市
         
         private String company; //公司
         
         private String accountName; //账户名
         
         private String accountBank; //开户行
         
         private String account; //账户
         
         private String accountNature; //账户性质
         
         private String tradeTime; //交易日期
         
         private Integer startBlack; //期初余额
         
         private Integer incom; //本期收入
         
         private Integer pay; //本期支出
         
         private Integer endBlack; //期末余额
         
         private String abstrac; //摘要
         
         private String classify; //项目分类
          private  String remarks; //备注
*/    	 
         private String info;  //字段json
         
    	 private String createTime; //创建时间
    	 
    	 private String updateTime; //更新时间
    	 
    	 private String  uId;    //提交人id
    	
         private Integer year;   //年份
    	 
    	 private Integer month; //月份
    
    	 private Integer status; //状态
    	 
    	 
    	 public Capital() {
             super();
         }
         
         public  Capital(String id,String info,String createTime,String updateTime,String uId,
                 Integer year,Integer month,Integer status){
             this.id=id;
             this.info=info;
             this.createTime=createTime;
             this.updateTime=updateTime;
             this.uId=uId;
             this.year=year;
             this.month=month;
             this.status=status;
         }
    
    	public String getId() {
    		return id;
    	}
    
    	public void setId(String id) {
    		this.id = id;
    	}
    
    	public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getuId() {
    		return uId;
    	}
    
    	public void setuId(String uId) {
    		this.uId = uId;
    	}

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
       
}
