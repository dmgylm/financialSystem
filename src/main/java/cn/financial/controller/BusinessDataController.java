package cn.financial.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.Business;
import cn.financial.model.BusinessData;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.service.BusinessDataService;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.UuidUtil;

/**
 * 损益表Controller
 * @author lmn
 *
 */
@Controller
@RequestMapping("/businessData")
public class BusinessDataController {
 
    
        @Autowired
        private  BusinessDataService businessDataService;
        
        @Autowired
        private  UserOrganizationService userOrganizationService;  //角色的权限
        
        @Autowired
        private  OrganizationService organizationService;  //组织架构
        
        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
        
        /**
         * 根据条件查损益数据(提交不传就是查询全部)
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequiresPermissions("businessData:view")
        @RequestMapping(value="/listBy", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> listBusinessDataBy(HttpServletRequest request,Integer page,Integer pageSize) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                List<UserOrganization> userOrganization= userOrganizationService.listUserOrganization(uId); //判断 权限的数据
               /* for (int i = 0; i < userOrganization.size(); i++) {
                    List<Organization> listTreeByIdForSon=organizationService.listTreeByIdForSon(userOrganization.get(i).getoId()); //根据oId查出公司以下的部门
                    JSONArray listTreeByIdForSonJson=JSONArray.fromObject(listTreeByIdForSon);
                    System.out.println(listTreeByIdForSonJson);
                }*/
                if(request.getParameter("year")!=null && !request.getParameter("year").equals("")){
                    map.put("year",request.getParameter("year")); //年份
                }
                if(request.getParameter("month")!=null && !request.getParameter("month").equals("")){
                    map.put("month",request.getParameter("month"));  //月份
                }
                if(request.getParameter("sId")!=null && !request.getParameter("sId").equals("")){
                    map.put("sId",request.getParameter("sId"));  //月份
                }
               
                List<BusinessData> list = businessDataService.listBusinessDataBy(map); //查询所有符合搜索条件的表数据
                List<BusinessData> businessData=new ArrayList<>();  //所有符合权限的数据
                for (int i = 0; i < userOrganization.size(); i++) { //循环权限全部数据    
                    String id=userOrganization.get(i).getoId(); //找到权限数据里面的oId
                    //找权限的oId和损益表的oId进行筛选
                    for (int j = 0; j < list.size(); j++) {
                        String arrId=list.get(j).getoId();//找损益表里面的oId
                        if(id.equals(arrId)){  //判断权限oId 和全部数据的oId是否相同  
                           businessData.add(list.get(j));  // 可以显示的损益数据
                        }
                    }
                }
                //根据oId查询部门信息
                //循环合格数据的oid 去查询他的所有部门
                List<Business> businessList=new ArrayList<>();//页面列表排列数据
                for (int i = 0; i < businessData.size(); i++) {
                    List<Organization> listTreeByIdForSon=organizationService.listTreeByIdForSon(businessData.get(i).getoId()); //根据oId查出公司以下的部门
                    for (int j = 0; j < listTreeByIdForSon.size(); j++) {
                        if(listTreeByIdForSon.get(j).getOrgType()==3){ //找到公司以下的节点业务
                          if(!listTreeByIdForSon.get(j).getOrgName().contains("汇总")){  //含有 汇总的不是业务方式 
                              Business business=new Business();
                              business.setYear(businessData.get(i).getYear()); //年份
                              business.setMonth(businessData.get(i).getMonth()); //月份
                              business.setUserName(user.getName()); //用户
                              business.setUpdateTime(businessData.get(i).getUpdateTime()); //操作时间
                              business.setStatus(businessData.get(i).getStatus());//状态
                              Organization CompanyName= organizationService.getCompanyNameBySon(businessData.get(i).getoId());//查询所属的公司名
                              business.setCompany(CompanyName.getOrgName()); //公司
                              business.setStructures(listTreeByIdForSon.get(j).getOrgName()); //业务方式
                              businessList.add(business); 
                          }
                        }
                    }
                }   
                //数据分页
                if(businessList.size()>0){  //判断是否有符合权限的数据  没有则是抛出异常  有就进行数据分页传到前台
                    Integer p=(page - 1) * pageSize; //开始下标
                    Integer s=page* pageSize;  //结束下标
                    Integer totalPage = businessList.size() / pageSize; //总页数
                    if (businessList.size() % pageSize != 0){
                        totalPage++;
                    }
                    List<Business> subList =new ArrayList<>();
                    if(businessList.size()<pageSize){    //判断总得数据长度是否小于每页大小
                        subList=businessList.subList(0,businessList.size());
                    }else if(businessList.size()<s){     //判断总得数据长度是否小于结束下标大小
                        subList=businessList.subList(p,businessList.size());
                    }else{
                        subList=businessList.subList(p,s);
                    }
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                    dataMap.put("data", subList);
                    dataMap.put("totalPage", totalPage);
                }else{
                    throw new Exception("您没有权限查看损益表数据！");
                }
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
        @RequiresPermissions("businessData:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> selectBusinessDataById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                BusinessData  businessData=businessDataService.selectBusinessDataById(id);
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("resultData", businessData);
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
        @RequiresPermissions("businessData:create")
        @RequestMapping(value="/insert", method = RequestMethod.POST)
        public Map<String, Object> insertBusinessData(HttpServletRequest request,BusinessData businessData){
            Map<String, Object> dataMap = new HashMap<String, Object>();
           try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                businessData.setId(UuidUtil.getUUID());
                businessData.setuId(uId);
                businessData.setStatus(2);
                businessData.setDelStatus(1);
                Integer i = businessDataService.insertBusinessData(businessData);
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
        @RequiresPermissions("businessData:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
        public Map<String, Object> updateBusinessData(HttpServletRequest request,BusinessData businessData) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                businessData.setuId(uId);
                businessData.setDelStatus(1);
                Integer i = businessDataService.updateBusinessData(businessData);
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
        @RequiresPermissions("businessData:update")
        @RequestMapping(value="/delete", method = RequestMethod.POST)
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            String id = request.getParameter("id");
            try {
                Integer i =businessDataService.deleteBusinessData(id);
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
        @RequiresPermissions("businessData:download")
        @RequestMapping(value="/export",method = RequestMethod.POST)
        @ResponseBody
        public void export(HttpServletRequest request,HttpServletResponse response,String id) throws Exception{
            OutputStream os = null;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                BusinessData  statement=statementService.selectStatementById(id);
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
