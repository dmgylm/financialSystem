package cn.financial.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.Capital;
import cn.financial.model.Statement;
import cn.financial.model.User;
import cn.financial.service.StatementService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.ExcelUtil;
import cn.financial.util.UuidUtil;

/**
 * 损益表Controller
 * @author lmn
 *
 */
@Controller
@RequestMapping("/statement")
public class StatementController {
 
    
        @Autowired
        private  StatementService statementService;

        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
        
        /**
         * 根据条件查损益数据(提交不传就是查询全部)
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequiresPermissions("statement:view")
        @RequestMapping(value="/listBy", method = RequestMethod.POST)
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
                if(request.getParameter("sId")!=null && !request.getParameter("sId").equals("")){
                    map.put("sId",Integer.getInteger(request.getParameter("sId")));
                }
                if(request.getParameter("orgName")!=null && !request.getParameter("orgName").equals("")){
                    map.put("orgName",new String(request.getParameter("orgName").getBytes("ISO-8859-1"), "UTF-8"));
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
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("resultData", list);
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
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
        @RequiresPermissions("statement:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
        public Map<String, Object> selectStatementById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Statement  statement=statementService.selectStatementById(id);
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                
                dataMap.put("resultData", statement);
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                
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
        @RequiresPermissions("statement:create")
        @RequestMapping(value="/insert", method = RequestMethod.POST)
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
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                    
                } else {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                    
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
               
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 修改损益数据
         * @param request
         * @return
         */
        @RequiresPermissions("statement:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
        public Map<String, Object> updateStatement(HttpServletRequest request,Statement statement) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                statement.setuId(uId);
                statement.setDelStatus(1);
                Integer i = statementService.updateStatement(statement);
                if (i == 1) {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                    
                } else {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                    
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
               
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        /**
         * 删除损益数据 （修改Status为0）
         * @param request
         * @return
         */
        @RequiresPermissions("statement:update")
        @RequestMapping(value="/delete", method = RequestMethod.POST)
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            String id = request.getParameter("id");
            try {
                Integer i =statementService.deleteStatement(id);
                if (i == 1) {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                    
                } else {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                    
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
        
       /* *//***
         * 导出下载
         * @param response
         * @throws Exception 
         *//*
        @RequiresPermissions("statement:download")
        @RequestMapping(value="/export",method = RequestMethod.POST)
        @ResponseBody
        public void export(HttpServletRequest request,HttpServletResponse response,String id) throws Exception{
            OutputStream os = null;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Statement  statement=statementService.selectStatementById(id);
                List<String[]> strList=new ArrayList<>();
                String[] ss={"模板","事业部","大区名称","省份","城市","公司名称","户名","开户行","账户","账户性质",
                        "交易日期","期初余额","本期收入","本期支出","期末余额","摘要","项目分类","备注"};
                strList.add(ss);
                for (int i = 0; i < list.size(); i++) {
                    String[] str=new String[18];
                    Capital capital=list.get(i);
                    if(!capital.getPlate().equals("")){
                        str[0]=capital.getPlate();
                     }
                    if(!capital.getBU().equals("")){
                        str[1]=capital.getBU();
                     }
                    if(!capital.getRegionName().equals("")){
                        str[2]=capital.getRegionName();
                     }
                    if(!capital.getProvince().equals("")){
                        str[3]=capital.getProvince();
                     }
                    if(!capital.getCity().equals("")){
                        str[4]=capital.getCity();
                     }
                    if(!capital.getCompany().equals("")){
                        str[5]=capital.getCompany();
                     }
                    if(!capital.getAccountName().equals("")){
                       str[6]=capital.getAccountName();
                    }
                    if(!capital.getAccountBank().equals("")){
                       str[7]=capital.getAccountBank();
                    }
                    if(!capital.getAccount().equals("")){
                       str[8]=capital.getAccount();
                    }
                    if(!capital.getAccountNature().equals("")){
                       str[9]=capital.getAccountNature();
                    }
                    if(!capital.getTradeTime().equals("")){
                       str[10]=capital.getTradeTime();
                    }
                    if(!capital.getStartBlack().equals("")){
                       str[11]=capital.getStartBlack().toString();
                    }
                    if(!capital.getIncom().equals("")){
                       str[12]=capital.getIncom().toString();
                    }
                    if(!capital.getPay().equals("")){
                       str[13]=capital.getPay().toString();
                    }
                    if(!capital.getEndBlack().equals("")){
                       str[14]=capital.getEndBlack().toString();
                    }
                    if(!capital.getAbstrac().equals("")){
                        str[15]=capital.getAbstrac();
                    }
                    if(!capital.getClassify().equals("")){
                        str[16]=capital.getClassify();
                    }
                    if(!capital.getRemarks().equals("")){
                        str[17]=capital.getRemarks();
                    } 
                        strList.add(str);
                }
                response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode("资金流水表", "UTF-8")+".xls");
                response.setContentType("application/octet-stream");
                os = response.getOutputStream();
                ExcelUtil.export(strList, os);
                dataMap.put("resultCode", 200);
                dataMap.put("result", "导出成功!");
            } catch (IOException e) {
                dataMap.put("resultCode", 400);
                dataMap.put("result", "导出失败!");
                e.printStackTrace();
            } finally {
                if(os != null)
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }*/
}
