package cn.financial.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import cn.financial.model.Statement;
import cn.financial.model.User;
import cn.financial.service.StatementService;
import cn.financial.util.UuidUtil;

/**
 * 损益表Controller
 * @author lmn
 *
 */
@Controller
@RequestMapping("/tering")
public class StatementController {
 
    
        @Autowired
        private  StatementService statementService;

        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        /**
         * 根据条件查损益数据(提交不传就是查询全部)
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequiresPermissions("tering:view")
        @RequestMapping(value="/statement/listBy", method = RequestMethod.POST)
        public Map<String, Object> listStatementBy(HttpServletRequest request) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                if(request.getParameter("id")!=null && !request.getParameter("id").equals("")){
                   map.put("id", request.getParameter("id"));
                }
                if(request.getParameter("oId")!=null && !request.getParameter("oId").equals("")){
                    map.put("oId",request.getParameter("oId"));
                }
                if(request.getParameter("info")!=null && !request.getParameter("info").equals("")){
                    map.put("info",request.getParameter("info"));
                }
                if(request.getParameter("createTime")!=null && !request.getParameter("createTime").equals("")){
                    map.put("createTime",request.getParameter("createTime"));
                }
                if(request.getParameter("updateTime")!=null && !request.getParameter("updateTime").equals("")){
                    map.put("updateTime",request.getParameter("updateTime"));
                }
                if(request.getParameter("typeId")!=null && !request.getParameter("typeId").equals("")){
                    map.put("typeId",request.getParameter("typeId"));
                }
                if(uId!=null && !uId.equals("")){
                    map.put("uId",uId);
                }
                if(request.getParameter("year")!=null && !request.getParameter("year").equals("")){
                    map.put("year",Integer.getInteger(request.getParameter("year")));
                }
                if(request.getParameter("month")!=null && !request.getParameter("month").equals("")){
                    map.put("month",Integer.getInteger(request.getParameter("month")));
                }
                if(request.getParameter("status")!=null && !request.getParameter("status").equals("")){
                    map.put("status",Integer.getInteger(request.getParameter("status")));
                }
                if(request.getParameter("delStatus")!=null && !request.getParameter("delStatus").equals("")){
                    map.put("delStatus",Integer.getInteger(request.getParameter("delStatus")));
                }
                Integer pageSize=0;
                if(request.getParameter("pageSize")!=null && !request.getParameter("pageSize").equals("")){
                    pageSize=Integer.parseInt(request.getParameter("pageSize"));
                    map.put("pageSize",pageSize);
                }
                Integer start=0;
                if(request.getParameter("page")!=null && !request.getParameter("page").equals("")){
                    start=pageSize * (Integer.parseInt(request.getParameter("page")) - 1);
                    map.put("start",start);
                }
                List<Statement> list = statementService.listStatementBy(map);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功!");
                dataMap.put("resultData", list);
            } catch (Exception e) {
                dataMap.put("resultCode", 400);
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
        @RequiresPermissions("tering:view")
        @RequestMapping(value="/statement/listById", method = RequestMethod.POST)
        public Map<String, Object> selectStatementById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Statement  statement=statementService.selectStatementById(id);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功!");
                dataMap.put("resultData", statement);
            } catch (Exception e) {
                dataMap.put("resultCode", 400);
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
        @RequiresPermissions("tering:create")
        @RequestMapping(value="/statement/insert", method = RequestMethod.POST)
        public Map<String, Object> insertStatement(HttpServletRequest request,Statement statement){
            Map<String, Object> dataMap = new HashMap<String, Object>();
           try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                statement.setId(UuidUtil.getUUID());
                statement.setuId(uId);
                statement.setStatus(2);
                statement.setDelStatus(1);
                Integer i = statementService.insertStatement(statement);
                if (i == 1) {
                    dataMap.put("resultCode", 200);
                    dataMap.put("result", "新增成功!");
                } else {
                    dataMap.put("resultCode", 400);
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
        @RequiresPermissions("tering:update")
        @RequestMapping(value="/statement/update", method = RequestMethod.POST)
        public Map<String, Object> updateStatement(HttpServletRequest request,Statement statement) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                statement.setuId(uId);
                statement.setDelStatus(1);
                Integer i = statementService.updateStatement(statement);
                if (i == 1) {
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "修改成功!");
                } else {
                    dataMap.put("resultCode", 400);
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
        @RequiresPermissions("tering:update")
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
                    dataMap.put("resultCode", 400);
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
