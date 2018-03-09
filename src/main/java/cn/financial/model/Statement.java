package cn.financial.model;

import java.util.Date;

/**
 * 损益表model
 * @author lmn
 *
 */
public class Statement {
	
        private String id;   //损益表id
            	 
        private String oId; //组织id
            	 
        private String info;  //内容
            	 
        private Date createTime; //创建时间
            	 
        private Date updateTime; //更新时间
            	 
        private String typeId;    //版块Id(来源于组织结构表）
            	 
        private String uId; // 提交人id
            	
        private Integer year;   //年份
            	 
        private Integer month; //月份
        
        private Integer status; //状态
    
        public Statement() {
            super();
        }
        
        public  Statement(String id,String oId,String info,Date createTime,Date updateTime,
                String typeId,String uId,Integer year,Integer month,Integer status){
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

}
