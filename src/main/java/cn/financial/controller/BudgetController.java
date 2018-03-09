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
import cn.financial.model.Budget;
import cn.financial.service.BudgetService;
import cn.financial.util.UuidUtil;

/**
 * 预算表Controller
 * @author lmn
 *
 */
@Controller
public class BudgetController {
 
    
        @Autowired
        private  BudgetService budgetService;

        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
      
        /**
         * 查询所有的预算数据
         * 
         * @param request
         * @param response
         */
        @RequestMapping(value="/budget/list", method = RequestMethod.GET)
        public Map<String, Object> getAllbudget(HttpServletRequest request, HttpServletResponse response) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<Budget> list = budgetService.getAllBudget();
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
         * 根据条件查预算数据
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequestMapping(value="/budget/listBy", method = RequestMethod.GET)
        public Map<String, Object> listbudgetBy(HttpServletRequest request, Map<Object, Object> map) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<Budget> list = budgetService.listBudgetBy(map);
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
         * 根据id查询预算数据
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @RequestMapping(value="/budget/listById", method = RequestMethod.GET)
        public Map<String, Object> selectbudgetById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Budget  budget=budgetService.selectBudgetById(id);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功!");
                dataMap.put("resultData", budget);
            } catch (Exception e) {
                dataMap.put("resultCode", 200);
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
        @RequestMapping(value="/budget/insert", method = RequestMethod.GET)
        public Map<String, Object> insertbudget(HttpServletRequest request, HttpServletResponse response){
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
                Budget budget =new Budget();
                budget.setId(id);
                budget.setoId(oId);
                budget.setInfo(info);
                budget.setCreateTime(sdf.parse(createTime));
                budget.setUpdateTime(sdf.parse(updateTime));
                budget.setTypeId(typeId);
                budget.setuId(uId);
                budget.setYear(year);
                budget.setMonth(month);
                budget.setStatus(status);
                Integer i = budgetService.insertBudget(budget);
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
         * 修改预算数据
         * @param request
         * @return
         */
        @RequestMapping(value="/budget/update", method = RequestMethod.GET)
        public Map<String, Object> updatebudget(HttpServletRequest request) {
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
                Budget budget =new Budget();
                budget.setId(id);
                budget.setoId(oId);
                budget.setInfo(info);
                budget.setCreateTime(sdf.parse(createTime));
                budget.setUpdateTime(sdf.parse(updateTime));
                budget.setTypeId(typeId);
                budget.setuId(uId);
                budget.setYear(year);
                budget.setMonth(month);
                budget.setStatus(status);
                Integer i = budgetService.updateBudget(budget);
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
         * 删除预算数据 （修改Status为0）
         * @param request
         * @return
         */
        @RequestMapping(value="/budget/delete", method = RequestMethod.GET)
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Budget budget =new Budget();
                budget.setId(id);
                budget.setoId(oId);
                budget.setInfo(info);
                budget.setCreateTime(sdf.parse(createTime));
                budget.setUpdateTime(sdf.parse(updateTime));
                budget.setTypeId(typeId);
                budget.setuId(uId);
                budget.setYear(year);
                budget.setMonth(month);
                budget.setStatus(0);
                Integer i = budgetService.updateBudget(budget);
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
