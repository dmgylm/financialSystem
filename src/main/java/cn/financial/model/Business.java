package cn.financial.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 损益所需要的列表集合
 * @author lmn
 *
 */
@ApiModel(value="Business对象",description="Business对象")
public class Business {
    
        @ApiModelProperty(value="id",name="id")
        private String id;   //id
        
        @ApiModelProperty(value="年份",name="year")
        private Integer year;   //年份
            
        @ApiModelProperty(value="月份",name="month")
        private Integer month; //月份
        
        @ApiModelProperty(value="公司",name="company")
        private String company; //公司
        
        @ApiModelProperty(value="业务方式",name="structures")
        private String structures; //公司业务方式
        
        @ApiModelProperty(value="用户名",name="userName")
        private String userName; //用户
        
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        @ApiModelProperty(value="更新时间操作时间",name="updateTime")
        private Date updateTime; //操作时间
        
        @ApiModelProperty(value="状态 （0待提交  1已提交 2新增  3退回）",name="status")
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

       
}
