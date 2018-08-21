package cn.financial.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 损益表model
 * @author lmn
 *
 */
public class BusinessData {
    
        public static final Integer DEPNUM = 3;  //orgType常量
        public static final Integer ORGNUM = 2;  //orgType常量
        
        public static final String NAME = "汇总";  //name常量
	 
        private String id;   //损益表id
            	 
        private String oId; //组织id
            	 
        private String info;  //内容
            	 
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        private Date createTime; //创建时间
            	 
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        private Date updateTime; //更新时间
            	 
        private String typeId;    //版块Id(来源于组织结构表）
            	 
        private String uId; // 提交人id

        private String dataModuleId; // 数据模板id
            	
        private Integer year;   //年份
            	 
        private Integer month; //月份
        
        private Integer status; //提交状态（0 待提交   1待修改  2已提交  3新增 4 退回修改）
        
        private Integer delStatus; //删除状态（0已删除 1未删除）
        
        private Integer sId; //1表示损益表   2表示预算表
        
        private Integer version; //版本号，每当有退回时候 版本号自动加1
        
        private String  orgName; //组织架构
    
        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public BusinessData() {
            super();
        }
        
        public  BusinessData(String id,String oId,String info,Date createTime,Date updateTime,
                String typeId,String uId,Integer year,Integer month,Integer status, Integer delStatus,Integer sId,Integer version){
            this.id=id;
            this.oId=oId;
            this.info=info;
            this.createTime=createTime;
            this.updateTime=updateTime;
            this.typeId=typeId;
            this.uId=uId;
            this.year=year;
            this.month=month;
            this.status=status;
            this.delStatus=delStatus;
            this.sId=sId;
            this.version=version;
        }
         
    	public String getId() {
    		return id;
    	}
    
    	public void setId(String id) {
    		this.id = id;
    	}
    
    	public String getoId() {
    		return oId;
    	}
    
    	public void setoId(String oId) {
    		this.oId = oId;
    	}
    
    	public String getInfo() {
    		return info;
    	}
    
    	public void setInfo(String info) {
    		this.info = info;
    	}
    
    

        public String getTypeId() {
    		return typeId;
    	}
    
    	public void setTypeId(String typeId) {
    		this.typeId = typeId;
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

        public Integer getDelStatus() {
            return delStatus;
        }

        public void setDelStatus(Integer delStatus) {
            this.delStatus = delStatus;
        }

        public Integer getsId() {
            return sId;
        }

        public void setsId(Integer sId) {
            this.sId = sId;
        }

		public String getDataModuleId() {
			return dataModuleId;
		}

		public void setDataModuleId(String dataModuleId) {
			this.dataModuleId = dataModuleId;
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

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }
        

}
