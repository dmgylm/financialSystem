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
import cn.financial.model.Budget;
import cn.financial.model.User;
import cn.financial.service.BudgetService;
import cn.financial.util.UuidUtil;

/**
 * 预算表Controller
 * @author lmn
 *
 */
@Controller
@RequestMapping("/tering")
public class BudgetController {
 
    
        @Autowired
        private  BudgetService budgetService;

        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     
        /**
         * 根据条件查预算数据
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequiresPermissions("tering:view")
        @RequestMapping(value="/budget/listBy", method = RequestMethod.POST)
        public Map<String, Object> listbudgetBy(HttpServletRequest request) {
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
                    map.put("createTime",sdf.parse(request.getParameter("createTime")));
                }
                if(request.getParameter("updateTime")!=null && !request.getParameter("updateTime").equals("")){
                    map.put("updateTime",sdf.parse(request.getParameter("updateTime")));
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
                List<Budget> list = budgetService.listBudgetBy(map);
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
         * 根据id查询预算数据
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @RequiresPermissions("tering:view")
        @RequestMapping(value="/budget/listById", method = RequestMethod.POST)
        public Map<String, Object> selectbudgetById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Budget  budget=budgetService.selectBudgetById(id);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功!");
                dataMap.put("resultData", budget);
            } catch (Exception e) {
                dataMap.put("resultCode", 400);
                dataMap.put("resultDesc", "查询失败!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 新增预算数据
         * @param request
         * @param response
         * @return
         */
        @RequiresPermissions("tering:create")
        @RequestMapping(value="/budget/insert", method = RequestMethod.POST)
        public Map<String, Object> insertbudget(HttpServletRequest request, HttpServletResponse response,Budget budget){
            Map<String, Object> dataMap = new HashMap<String, Object>();
           try {
               User user = (User) request.getAttribute("user");
               String uId = user.getId();
               budget.setId(UuidUtil.getUUID());
               budget.setuId(uId);
               budget.setCreateTime(new Date());
               budget.setDelStatus(1);
                Integer i = budgetService.insertBudget(budget);
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
         * 修改预算数据
         * @param request
         * @return
         */
        @RequiresPermissions("tering:update")
        @RequestMapping(value="/budget/update", method = RequestMethod.POST)
        public Map<String, Object> updatebudget(HttpServletRequest request,Budget budget) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                budget.setuId(uId);
                budget.setCreateTime(new Date());
                budget.setDelStatus(1);
                Integer i = budgetService.updateBudget(budget);
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
         * 删除预算数据 （修改Status为0）
         * @param request
         * @return
         */
        @RequiresPermissions("tering:update")
        @RequestMapping(value="/budget/delete", method = RequestMethod.POST)
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            try {
                String id = request.getParameter("id");
                Integer i = budgetService.deleteBudget(id);
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
