package cn.financial.model;

import java.util.Date;

/**
 * 资金表model
 * @author lmn
 *
 */
public class Capital {
	
         private String id;   //资金表id
         
         private String oId;  //组织架构id
    	 
         private String plate; //板块
         
         private String BU; //事业部
         
         private String regionName; //大区名称
         
         private String province; //省份
         
         private String city; //城市
         
         private String company; //公司
         
         private String accountName; //账户名
         
         private String accountBank; //开户行
         
         private String account; //账户
         
         private String accountNature; //账户性质
         
         private Date tradeTime; //交易日期
         
         private Integer startBlack; //期初余额
         
         private Integer incom; //本期收入
         
         private Integer pay; //本期支出
         
         private Integer endBlack; //期末余额
         
         private String abstrac; //摘要
         
         private String classify; //项目分类
         
         private  String remarks; //备注
         
    	 private String createTime; //创建时间
    	 
    	 private String updateTime; //更新时间
    	 
    	 private String  uId;    //提交人id
    	
         private Integer year;   //年份
    	 
    	 private Integer month; //月份
    
    	 private Integer status; //状态
    	 
    	 
    	 public Capital() {
             super();
         }
         
         public  Capital(String id,String oId,String plate,String BU,String regionName,String province,String city,String company,String accountBank,String accountName,String account,String accountNature,
                 Date tradeTime,Integer startBlack,Integer incom,Integer pay,Integer endBlack,String abstrac,String classify,
                 String createTime,String updateTime,String uId,Integer year,Integer month,String remarks,Integer status){
             this.id=id;
             this.oId=oId;
             this.plate=plate;
             this.BU=BU;
             this.regionName=regionName;
             this.province=province;
             this.city=city;
             this.company=company;
             this.accountName=accountName;
             this.accountBank=accountBank;
             this.account=account;
             this.accountNature=accountNature;
             this.tradeTime=tradeTime;
             this.startBlack=startBlack;
             this.incom=incom;
             this.pay=pay;
             this.endBlack=endBlack;
             this.abstrac=abstrac;
             this.classify=classify;
             this.createTime=createTime;
             this.updateTime=updateTime;
             this.uId=uId;
             this.year=year;
             this.month=month;
             this.remarks=remarks;
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

        public String getPlate() {
            return plate;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }

        public String getBU() {
            return BU;
        }

        public void setBU(String bU) {
            BU = bU;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountBank() {
            return accountBank;
        }

        public void setAccountBank(String accountBank) {
            this.accountBank = accountBank;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getAccountNature() {
            return accountNature;
        }

        public void setAccountNature(String accountNature) {
            this.accountNature = accountNature;
        }

        

        public Date getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(Date tradeTime) {
            this.tradeTime = tradeTime;
        }

        public Integer getStartBlack() {
            return startBlack;
        }

        public void setStartBlack(Integer startBlack) {
            this.startBlack = startBlack;
        }

        public Integer getIncom() {
            return incom;
        }

        public void setIncom(Integer incom) {
            this.incom = incom;
        }

        public Integer getPay() {
            return pay;
        }

        public void setPay(Integer pay) {
            this.pay = pay;
        }

        public Integer getEndBlack() {
            return endBlack;
        }

        public void setEndBlack(Integer endBlack) {
            this.endBlack = endBlack;
        }

        public String getAbstrac() {
            return abstrac;
        }

        public void setAbstrac(String abstrac) {
            this.abstrac = abstrac;
        }

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getoId() {
            return oId;
        }

        public void setoId(String oId) {
            this.oId = oId;
        }

}
