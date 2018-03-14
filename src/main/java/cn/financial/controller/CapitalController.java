package cn.financial.controller;

import java.util.Date;
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
import cn.financial.model.Capital;
import cn.financial.service.CapitalService;
import cn.financial.util.UuidUtil;

/**
 * 资金表Controller
 * @author lmn
 *
 */
@Controller
public class CapitalController {
 
    
        @Autowired
        private  CapitalService capitalService;

        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
      
        /**
         * 查询所有的资金数据
         * 
         * @param request
         * @param response
         */
        @RequestMapping(value="/capital/list", method = RequestMethod.POST)
        public Map<String, Object> getAllCapital(HttpServletRequest request, HttpServletResponse response) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<Capital> list = capitalService.getAllCapital();
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
         * 根据条件查资金数据
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequestMapping(value="/capital/listBy", method = RequestMethod.POST)
        public Map<String, Object> listCapitalBy(HttpServletRequest request, Map<Object, Object> map) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<Capital> list = capitalService.listCapitalBy(map);
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
         * 根据id查询资金数据
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @RequestMapping(value="/capital/listById", method = RequestMethod.POST)
        public Map<String, Object> selectCapitalById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Capital  Capital=capitalService.selectCapitalById(id);
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询成功!");
                dataMap.put("resultData", Capital);
            } catch (Exception e) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "查询失败!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 新增资金数据
         * @param request
         * @param response
         * @return
         */
        @RequestMapping(value="/capital/insert", method = RequestMethod.POST)
        public Map<String, Object> insertCapital(HttpServletRequest request, HttpServletResponse response, Capital capital){
            Map<String, Object> dataMap = new HashMap<String, Object>();
           try {
                capital.setId(UuidUtil.getUUID());
                capital.setCreateTime(new Date());
                capital.setStatus(1);
                Integer i = capitalService.insertCapital(capital);
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
         * 修改资金数据
         * @param request
         * @return
         */
        @RequestMapping(value="/capital/update", method = RequestMethod.POST)
        public Map<String, Object> updateCapital(HttpServletRequest request,Capital capital) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                capital.setCreateTime(new Date());
                capital.setStatus(1);
                Integer i = capitalService.updateCapital(capital);
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
        @RequestMapping(value="/capital/delete", method = RequestMethod.POST)
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            String id = request.getParameter("id");
            try {
                Integer i =capitalService.deleteCapital(id);
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
