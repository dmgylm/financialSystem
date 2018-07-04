package cn.financial.model;

import java.util.Date;

/**
 * 损益所需要的列表集合
 * @author lmn
 *
 */
public class Business {
	
        private Integer year;   //年份
            	 
        private Integer month; //月份
        
        private String company; //公司
        
        private String structures; //公司业务方式
        
        private String userName; //用户
        
        private Date updateTime; //操作时间
        
        private Integer status; //状态 （0待提交  1已提交 2新增  3退回）

       

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

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getStructures() {
            return structures;
        }

        public void setStructures(String structures) {
            this.structures = structures;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public Integer getStatus() {
            return status;
        }
   
       
}
