package cn.financial.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import cn.financial.model.Statement;
import cn.financial.service.StatementService;
import cn.financial.util.UuidUtil;

/**
 * 损益表Controller
 * @author lmn
 *
 */
@Controller
public class StatementController {
 
    
        @Autowired
        private  StatementService statementService;

        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
      
        /**
         * 查询所有的损益数据
         * 
         * @param request
         * @param response
         */
        @RequestMapping(value="/statement/list", method = RequestMethod.POST)
        public Map<String, Object> getAllStatement(HttpServletRequest request, HttpServletResponse response) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<Statement> list = statementService.getAll();
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功!");
                dataMap.put("resultData", list);
            } catch (Exception e) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询失败!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }

        /**
         * 根据条件查损益数据
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequestMapping(value="/statement/listBy", method = RequestMethod.POST)
        public Map<String, Object> listStatementBy(HttpServletRequest request, Map<Object, Object> map) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<Statement> list = statementService.listStatementBy(map);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功!");
                dataMap.put("resultData", list);
            } catch (Exception e) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询失败!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 根据id查询损益数据
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @RequestMapping(value="/statement/listById", method = RequestMethod.POST)
        public Map<String, Object> selectStatementById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Statement  statement=statementService.selectStatementById(id);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功!");
                dataMap.put("resultData", statement);
            } catch (Exception e) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询失败!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 新增损益数据
         * @param request
         * @param response
         * @return
         */
        @RequestMapping(value="/statement/insert", method = RequestMethod.POST)
        public Map<String, Object> insertStatement(HttpServletRequest request, HttpServletResponse response){
            Map<String, Object> dataMap = new HashMap<String, Object>();
           try {
                String id = UuidUtil.getUUID();
                String oId=request.getParameter("oId");
                String info=new String(request.getParameter("info").getBytes("ISO-8859-1"), "UTF-8");
                String createTime=request.getParameter("createTime");
                String updateTime=request.getParameter("");
                String typeId=request.getParameter("updateTime");
                String uId=request.getParameter("uId");
                Integer year=Integer.parseInt(request.getParameter("year"));
                Integer month=Integer.parseInt(request.getParameter("month"));
                Integer status=Integer.parseInt(request.getParameter("status"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Statement statement =new Statement();
                statement.setId(id);
                statement.setoId(oId);
                statement.setInfo(info);
                statement.setCreateTime(sdf.parse(createTime));
                statement.setUpdateTime(sdf.parse(updateTime));
                statement.setTypeId(typeId);
                statement.setuId(uId);
                statement.setYear(year);
                statement.setMonth(month);
                statement.setStatus(status);
                Integer i = statementService.insertStatement(statement);
                if (i == 1) {
                    dataMap.put("resultCode", 200);
                    dataMap.put("result", "新增成功!");
                } else {
                    dataMap.put("resultCode", 200);
                    dataMap.put("result", "新增失败!");
                }
            } catch (Exception e) {
                dataMap.put("resultCode", 500);
                dataMap.put("resultDesc", "服务器异常!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 修改损益数据
         * @param request
         * @return
         */
        @RequestMapping(value="/statement/update", method = RequestMethod.POST)
        public Map<String, Object> updateStatement(HttpServletRequest request) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                String id = request.getParameter("id");
                String oId=request.getParameter("oId");
                String info=new String(request.getParameter("info").getBytes("ISO-8859-1"), "UTF-8");
                String createTime=request.getParameter("createTime");
                String updateTime=request.getParameter("");
                String typeId=request.getParameter("updateTime");
                String uId=request.getParameter("uId");
                Integer year=Integer.parseInt(request.getParameter("year"));
                Integer month=Integer.parseInt(request.getParameter("month"));
                Integer status=Integer.parseInt(request.getParameter("status"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Integer i = statementService.updateStatement(id, oId, info,sdf.parse(createTime),sdf.parse(updateTime),
                        typeId, uId, year, month, status);
                if (i == 1) {
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "修改成功!");
                } else {
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "修改失败!");
                }
            } catch (Exception e) {
                dataMap.put("resultCode", 500);
                dataMap.put("resultDesc", "服务器异常!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 删除损益数据 （修改Status为0）
         * @param request
         * @return
         */
        @RequestMapping(value="/statement/delete", method = RequestMethod.POST)
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            String id = request.getParameter("id");
            try {
                Integer i =statementService.deleteStatement(id);
                if (i == 1) {
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "删除成功!");
                } else {
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "删除失败!");
                }
            } catch (Exception e) {
                dataMap.put("resultCode", 500);
                dataMap.put("resultDesc", "服务器异常!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
}
