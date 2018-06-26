package cn.financial.model;

/**
 * 损益表model
 * @author lmn
 *
 */
public class BusinessData {
	
        private String id;   //损益表id
            	 
        private String oId; //组织id
            	 
        private String info;  //内容
            	 
        private String createTime; //创建时间
            	 
        private String updateTime; //更新时间
            	 
        private String typeId;    //版块Id(来源于组织结构表）
            	 
        private String uId; // 提交人id
            	
        private Integer year;   //年份
            	 
        private Integer month; //月份
        
        private Integer status; //提交状态 （0待提交  1已提交 2新增）
        
        private Integer delStatus; //删除状态（0已删除 1未删除）
        
        private Integer sId; //1表示损益表   2表示预算表
    
        public BusinessData() {
            super();
        }
        
        public  BusinessData(String id,String oId,String info,String createTime,String updateTime,
                String typeId,String uId,Integer year,Integer month,Integer status, Integer delStatus,Integer sId){
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
        

}
