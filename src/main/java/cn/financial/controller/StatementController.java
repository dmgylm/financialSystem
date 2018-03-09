package cn.financial.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
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

@Controller
public class StatementController {
 
    
        @Autowired
        private  StatementService statementService;

        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
      
        /**
         * 查询所有的
         * 
         * @param request
         * @param response
         */
        @RequestMapping(value="/statement/list", method = RequestMethod.GET)
        public Map<String, Object> getAllStatement(HttpServletRequest request, HttpServletResponse response) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<Statement> list = statementService.getAll();
                System.out.println(list.size());
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i).getId());
                }
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
         * 根据条件查
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequestMapping(value="/statement/listBy", method = RequestMethod.GET)
        public Map<String, Object> listStatementBy(HttpServletRequest request, Map<Object, Object> map) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<Statement> list = statementService.listStatementBy(map);
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i).getId());
                }
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
         * 根据ID查询
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @RequestMapping(value="/statement/listById", method = RequestMethod.GET)
        public Map<String, Object> selectStatementById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Statement  statement=statementService.selectStatementById(id);
                 System.out.println(statement.getId());
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
         * 新增
         * @param request
         * @param oId
         * @param info
         * @param createTime
         * @param updateTime
         * @param typeId
         * @param uId
         * @param year
         * @param month
         * @return
         * @throws ParseException 
         */
        @RequestMapping(value="/statement/insert", method = RequestMethod.GET)
        public Map<String, Object> insertStatement(HttpServletRequest request, HttpServletResponse response,String oId, String info,
               String createTime, String updateTime,String typeId, String uId,Integer year, Integer month,Integer status) 
              throws ParseException {
            try {
                info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
                oId = new String(oId.getBytes("ISO-8859-1"), "UTF-8");
                typeId = new String(typeId.getBytes("ISO-8859-1"), "UTF-8");
                uId = new String(uId.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Map<String, Object> dataMap = new HashMap<String, Object>();
            String id = UuidUtil.getUUID();
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
            try {
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
         * 根据条件修改
         * 
         * @param request
         * @param map
         *            传递的map必须包含有ID
         * @return
         */
        @RequestMapping(value="/statement/update", method = RequestMethod.GET)
        public Map<String, Object> updateStatement(HttpServletRequest request,String id,String oId, String info,
                String createTime, String updateTime,String typeId, String uId,Integer year, Integer month,Integer status) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
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
                Integer i = statementService.updateStatement(statement);
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
         * 根据条件删除
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @RequestMapping(value="/statement/delete", method = RequestMethod.GET)
        public Map<Object, Object> deleteOrganization(HttpServletRequest request,String id,String oId, String info,
                String createTime, String updateTime,String typeId, String uId,Integer year, Integer month,Integer status) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            try {
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
                statement.setStatus(0);
                Integer i = statementService.updateStatement(statement);
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
