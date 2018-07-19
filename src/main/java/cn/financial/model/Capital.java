package cn.financial.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资金表model
 * @author lmn
 *
 */
@ApiModel(value="Capital对象",description="Capital对象")
public class Capital {
    
         public static final Integer DEPNUM = 3;  //orgType常量
         public static final Integer ORGNUM = 2;  //orgType常量
         
         public static final String NAME = "汇总";  //name常量
    
         @ApiModelProperty(value="资金流水id",name="id")
         private String id;   //资金表id
         
         @ApiModelProperty(value="组织架构id",name="oId")
         private String oId;  //组织架构id
    	 
         @ApiModelProperty(value="公司",name="company")
         private String company; //公司
         
         @ApiModelProperty(value="账户名",name="accountName")
         private String accountName; //账户名
         
         @ApiModelProperty(value="开户行",name="accountBank")
         private String accountBank; //开户行
         
         @ApiModelProperty(value="账户",name="account")
         private String account; //账户
         
         @ApiModelProperty(value="账户性质",name="accountNature")
         private String accountNature; //账户性质
         
         @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
         @ApiModelProperty(value="交易日期",name="tradeTime")
         private Date tradeTime; //交易日期
         
         @ApiModelProperty(value="期初余额",name="startBlack")
         private Integer startBlack; //期初余额
         
         @ApiModelProperty(value="本期收入",name="incom")
         private Integer incom; //本期收入
         
         @ApiModelProperty(value="本期支出",name="pay")
         private Integer pay; //本期支出
         
         @ApiModelProperty(value="期末余额",name="endBlack")
         private Integer endBlack; //期末余额
         
         @ApiModelProperty(value="摘要",name="abstrac")
         private String abstrac; //摘要
         
         @ApiModelProperty(value="项目分类",name="classify")
         private String classify; //项目分类
         
         @ApiModelProperty(value="备注",name="remarks")
         private  String remarks; //备注
         
         @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
         @ApiModelProperty(value="创建时间",name="createTime")
    	 private Date createTime; //创建时间
    	 
         @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
         @ApiModelProperty(value="更新时间",name="updateTime")
    	 private Date updateTime; //更新时间
    	 
         @ApiModelProperty(value="提交人id",name="uId")
    	 private String  uId;    //提交人id
    	
         @ApiModelProperty(value="年份",name="year")
         private Integer year;   //年份
    	 
         @ApiModelProperty(value="月份",name="month")
    	 private Integer month; //月份
    
         @ApiModelProperty(value="状态（0 已经删除不存在了 1存在）",name="status")
    	 private Integer status; //状态（0 已经删除不存在了 1存在）
    	 
         @ApiModelProperty(value="是否可以编辑（默认为0   0 不可编辑  1可以编辑）",name="editor")
    	 private Integer editor; //是否可以编辑（默认为0   0 不可编辑  1可以编辑）
    	 
    	 public Capital() {
             super();
         }
         
         public  Capital(String id,String oId,String plate,String bu,String regionName,String province,String city,String company,String accountBank,String accountName,String account,String accountNature,
                 Date tradeTime,Integer startBlack,Integer incom,Integer pay,Integer endBlack,String abstrac,String classify,
                 Date createTime,Date updateTime,String uId,Integer year,Integer month,String remarks,Integer status,Integer editor){
             this.id=id;
             this.oId=oId;
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
             this.editor=editor;
         }
    
    	public String getId() {
    		return id;
    	}
    
    	public void setId(String id) {
    		this.id = id;
    	}

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
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

        public Integer getEditor() {
            return editor;
        }

        public void setEditor(Integer editor) {
            this.editor = editor;
        }
        
}
