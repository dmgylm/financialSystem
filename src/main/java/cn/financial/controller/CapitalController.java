package cn.financial.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import cn.financial.model.Capital;
import cn.financial.model.User;
import cn.financial.service.CapitalService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.ExcelUtil;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONObject;

/**
 * 资金表Controller
 * @author lmn
 *
 */
@Controller
@RequestMapping("/capital")
public class CapitalController {
 
    
        @Autowired
        private  CapitalService capitalService;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
        
        @RequestMapping(value="/excel", method = RequestMethod.GET)
        public String getexcel(HttpServletRequest request, HttpServletResponse response) {
            return "cel";
        }


        /**
         * 根据条件查资金数据 (不传数据就是查询所有的)
         * 
         * @param request
         * @param map
         *            
         * @return
         */
        @RequiresPermissions("capital:view")
        @RequestMapping(value="/listBy", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> listCapitalBy(HttpServletRequest request) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                Map<Object, Object> map = new HashMap<>();
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                if(request.getParameter("plate")!=null && !request.getParameter("plate").equals("")){
                    map.put("plate",request.getParameter("plate"));
                }
                if(request.getParameter("BU")!=null && !request.getParameter("BU").equals("")){
                    map.put("BU",request.getParameter("BU"));
                }
                if(request.getParameter("regionName")!=null && !request.getParameter("regionName").equals("")){
                    map.put("regionName",request.getParameter("regionName"));
                }
                if(request.getParameter("province")!=null && !request.getParameter("province").equals("")){
                    map.put("province",request.getParameter("province"));
                }
                if(request.getParameter("city")!=null && !request.getParameter("city").equals("")){
                    map.put("city",request.getParameter("city"));
                }
                if(request.getParameter("accountNature")!=null && !request.getParameter("accountNature").equals("")){
                    map.put("accountNature",request.getParameter("accountNature"));
                }
                //开始交易日期  
                if(request.getParameter("tradeTimeBeg")!=null && !request.getParameter("tradeTimeBeg").equals("")){
                    map.put("tradeTimeBeg",request.getParameter("tradeTimeBeg"));
                }
                //结束交易日期
                if(request.getParameter("tradeTimeEnd")!=null && !request.getParameter("tradeTimeEnd").equals("")){
                    map.put("tradeTimeEnd",request.getParameter("tradeTimeEnd"));
                }
                if(request.getParameter("classify")!=null && !request.getParameter("classify").equals("")){
                    map.put("classify",request.getParameter("classify"));
                }
                //提交人
                if(uId!=null && !uId.equals("")){
                    map.put("uId",uId);
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
                List<Capital> list = capitalService.listCapitalBy(map);
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("resultData", list);
                
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
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
        @RequiresPermissions("capital:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> selectCapitalById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                if(id!=null&&!id.equals("")){
                   Capital  Capital=capitalService.selectCapitalById(id);
                   dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                   dataMap.put("resultData", Capital);
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
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
        @RequiresPermissions("capital:create")
        @RequestMapping(value="/insert", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> insertCapital(HttpServletRequest request,HttpServletResponse response){
            Map<String, Object> dataMap = new HashMap<String, Object>();
           try {
               User user = (User) request.getAttribute("user");
               String uId = user.getId();
               Capital capital =new Capital();
               capital.setId(UuidUtil.getUUID());
               if(request.getParameter("plate")!=null && !request.getParameter("plate").equals("")){
                   capital.setPlate(new String(request.getParameter("plate").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("BU")!=null && !request.getParameter("BU").equals("")){
                   capital.setBU(new String(request.getParameter("BU").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("regionName")!=null && !request.getParameter("regionName").equals("")){
                   capital.setRegionName(new String(request.getParameter("regionName").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("province")!=null && !request.getParameter("province").equals("")){
                   capital.setProvince(new String(request.getParameter("province").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("city")!=null && !request.getParameter("city").equals("")){
                   capital.setCity(new String(request.getParameter("city").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("company")!=null && !request.getParameter("company").equals("")){
                   capital.setCompany(new String(request.getParameter("company").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("accountName")!=null && !request.getParameter("accountName").equals("")){
                   capital.setAccountName(new String(request.getParameter("accountName").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("accountBank")!=null && !request.getParameter("accountBank").equals("")){
                   capital.setAccountBank(new String(request.getParameter("accountBank").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("account")!=null && !request.getParameter("account").equals("")){
                   capital.setAccount(new String(request.getParameter("account").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("accountNature")!=null && !request.getParameter("accountNature").equals("")){
                   capital.setAccountNature(new String(request.getParameter("accountNature").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("tradeTime")!=null && !request.getParameter("tradeTime").equals("")){
                   capital.setTradeTime(request.getParameter("tradeTime"));
               }
               if(request.getParameter("startBlack")!=null && !request.getParameter("startBlack").equals("")){
                   capital.setStartBlack(Integer.getInteger(request.getParameter("startBlack")));
               }
               if(request.getParameter("incom")!=null && !request.getParameter("incom").equals("")){
                   capital.setIncom(Integer.getInteger(request.getParameter("incom")));
               }
               if(request.getParameter("pay")!=null && !request.getParameter("pay").equals("")){
                   capital.setPay(Integer.getInteger(request.getParameter("pay")));
               }
               if(request.getParameter("endBlack")!=null && !request.getParameter("endBlack").equals("")){
                   capital.setEndBlack(Integer.getInteger(request.getParameter("endBlack")));
               }
               if(request.getParameter("abstrac")!=null && !request.getParameter("abstrac").equals("")){
                   capital.setAbstrac(new String(request.getParameter("abstrac").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("classify")!=null && !request.getParameter("classify").equals("")){
                   capital.setClassify(new String(request.getParameter("classify").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(request.getParameter("remarks")!=null && !request.getParameter("remarks").equals("")){
                   capital.setRemarks(new String(request.getParameter("remarks").getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(uId!=null && !uId.equals("")){
                   capital.setuId(uId);
               }
               if(request.getParameter("year")!=null && !request.getParameter("year").equals("")){
                   capital.setYear(Integer.getInteger(request.getParameter("year")));
               }
               if(request.getParameter("month")!=null && !request.getParameter("month").equals("")){
                  capital.setMonth(Integer.getInteger(request.getParameter("month")));
               }
               capital.setStatus(1);
               Integer i = capitalService.insertCapital(capital);
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
         * 修改资金数据
         * @param request
         * @return
         */
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> updateCapital(HttpServletRequest request) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                Capital capital =new Capital();
                if(request.getParameter("id")!=null && !request.getParameter("id").equals("")){
                    capital.setId(request.getParameter("id"));
                }
                if(request.getParameter("plate")!=null && !request.getParameter("plate").equals("")){
                    capital.setPlate(new String(request.getParameter("plate").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("BU")!=null && !request.getParameter("BU").equals("")){
                    capital.setBU(new String(request.getParameter("BU").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("regionName")!=null && !request.getParameter("regionName").equals("")){
                    capital.setRegionName(new String(request.getParameter("regionName").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("province")!=null && !request.getParameter("province").equals("")){
                    capital.setProvince(new String(request.getParameter("province").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("city")!=null && !request.getParameter("city").equals("")){
                    capital.setCity(new String(request.getParameter("city").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("company")!=null && !request.getParameter("company").equals("")){
                    capital.setCompany(new String(request.getParameter("company").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountName")!=null && !request.getParameter("accountName").equals("")){
                    capital.setAccountName(new String(request.getParameter("accountName").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountBank")!=null && !request.getParameter("accountBank").equals("")){
                    capital.setAccountBank(new String(request.getParameter("accountBank").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("account")!=null && !request.getParameter("account").equals("")){
                    capital.setAccount(new String(request.getParameter("account").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountNature")!=null && !request.getParameter("accountNature").equals("")){
                    capital.setAccountNature(new String(request.getParameter("accountNature").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("tradeTime")!=null && !request.getParameter("tradeTime").equals("")){
                    capital.setTradeTime(request.getParameter("tradeTime"));
                }
                if(request.getParameter("startBlack")!=null && !request.getParameter("startBlack").equals("")){
                    capital.setStartBlack(Integer.getInteger(request.getParameter("startBlack")));
                }
                if(request.getParameter("incom")!=null && !request.getParameter("incom").equals("")){
                    capital.setIncom(Integer.getInteger(request.getParameter("incom")));
                }
                if(request.getParameter("pay")!=null && !request.getParameter("pay").equals("")){
                    capital.setPay(Integer.getInteger(request.getParameter("pay")));
                }
                if(request.getParameter("endBlack")!=null && !request.getParameter("endBlack").equals("")){
                    capital.setEndBlack(Integer.getInteger(request.getParameter("endBlack")));
                }
                if(request.getParameter("abstrac")!=null && !request.getParameter("abstrac").equals("")){
                    capital.setAbstrac(new String(request.getParameter("abstrac").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("classify")!=null && !request.getParameter("classify").equals("")){
                    capital.setClassify(new String(request.getParameter("classify").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("remarks")!=null && !request.getParameter("remarks").equals("")){
                    capital.setRemarks(new String(request.getParameter("remarks").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(uId!=null && !uId.equals("")){
                    capital.setuId(uId);
                }
                if(request.getParameter("year")!=null && !request.getParameter("year").equals("")){
                    capital.setYear(Integer.getInteger(request.getParameter("year")));
                }
                if(request.getParameter("month")!=null && !request.getParameter("month").equals("")){
                   capital.setMonth(Integer.getInteger(request.getParameter("month")));
                }
                capital.setStatus(1);
                Integer i = capitalService.updateCapital(capital);
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
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/delete", method = RequestMethod.POST)
        @ResponseBody
        public Map<Object, Object> deleteOrganization(HttpServletRequest request) {
            Map<Object, Object> dataMap = new HashMap<Object, Object>();
            String id = request.getParameter("id");
            try {
                if(id!=null && !id.equals("")){
                Integer i =capitalService.deleteCapital(id);
                if (i == 1) {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                 } else {
                     dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                 }     
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
       
        /***
         * 导入
         */
        @RequiresPermissions("capital:import")
        @RequestMapping(value="/excelImport",method = RequestMethod.POST)
        @ResponseBody
        public void excelImport(MultipartFile uploadFile,HttpServletRequest request) throws IOException{
            Map<String, Object> dataMap = new HashMap<String, Object>();
            if(uploadFile.getSize()>0 && uploadFile.getSize()<5242880){  //判断文件大小是否是5M以下的
              try {
                  int row=1;
                  Integer a=0;
                  List<String []> list=ExcelUtil.read(uploadFile.getInputStream(), row);//读取excel表格数据
                  System.out.println(list.size());
                  for (int i = 0; i < list.size(); i++){
                      Capital capital=new Capital();
                      String[] str=list.get(i);
                      capital.setId(UuidUtil.getUUID());
                      capital.setPlate(str[0]);
                      capital.setBU(str[1]);
                      capital.setRegionName(str[2]);
                      capital.setProvince(str[3]);
                      capital.setCity(str[4]);
                      capital.setCompany(str[5]);
                      capital.setAccountName(str[6]);
                      capital.setAccountBank(str[7]);
                      capital.setAccount(str[8]);
                      capital.setAccountNature(str[9]);
                      capital.setTradeTime(str[10]);
                      capital.setStartBlack(Integer.parseInt(str[11]));
                      capital.setIncom(Integer.parseInt(str[12]));
                      capital.setPay(Integer.parseInt(str[13]));
                      capital.setEndBlack(Integer.parseInt(str[14]));
                      capital.setAbstrac(str[15]);
                      capital.setClassify(str[16]);
                      capital.setRemarks(str[17]);
                      Calendar calendar = Calendar.getInstance();
                      calendar.setTime(calendar.getTime());
                      User user = (User) request.getAttribute("user");
                      String uId = user.getId();
                      capital.setuId(uId);
                      capital.setYear(Calendar.YEAR);
                      capital.setMonth(Calendar.MONTH);
                      capital.setStatus(1);
                    /*  System.out.println("id:"+capital.getId()+"plate"+capital.getPlate()+"BU:"+capital.getBU()+"RegionName:"
                       +capital.getRegionName()+"Province:"+capital.getProvince()+"city:"+capital.getCity()+"Company:"
                       +capital.getCompany()+"AccountName:"+capital.getAccountName()+"AccountBank:"+capital.getAccountBank()+
                       "Account:"+capital.getAccount()+"AccountNature:"+capital.getAccountNature()+"TradeTime:"+capital.getTradeTime()
                       +"StartBlack:"+capital.getStartBlack()+"Incom:"+capital.getIncom()+"Pay:"+capital.getPay()
                       +"EndBlack:"+capital.getEndBlack()+"Abstrac:"+capital.getAbstrac()+"Classify:"+capital.getClassify()
                       +"Remarks:"+capital.getRemarks()+"uId:"+capital.getuId()+"Year:"+capital.getYear()+"Month:"+capital.getMonth());*/
                      a = capitalService.insertCapital(capital);
                 }
                if (a == 1) {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                } else {
                    dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                }
            } catch (Exception e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
                this.logger.error(e.getMessage(), e);
            }
        }else{
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.CAPITAL_FILE_EXCEED_5M));
        } 
      }
        
        
        /***
         * 导出
         * @param response
         * @throws Exception 
         */
        @RequiresPermissions("capital:download")
        @RequestMapping(value="/export",method = RequestMethod.POST)
        @ResponseBody
        public void export(HttpServletRequest request,HttpServletResponse response,String[] id) throws Exception{
            OutputStream os = null;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                List<String> ids = Arrays.asList(id);
                List<Capital> list = capitalService.listCapitalById(ids);
                List<String[]> strList=new ArrayList<>();
                String[] ss={"模板","事业部","大区名称","省份","城市","公司名称","户名","开户行","账户","账户性质",
                        "交易日期","期初余额","本期收入","本期支出","期末余额","摘要","项目分类","备注"};
                strList.add(ss);
                for (int i = 0; i < list.size(); i++) {
                    String[] str=new String[18];
                    Capital capital=list.get(i);
                    if(capital.getPlate()!=null &&!capital.getPlate().equals("")){
                        str[0]=capital.getPlate();
                     }
                    if(capital.getBU()!=null &&!capital.getBU().equals("")){
                        str[1]=capital.getBU();
                     }
                    if(capital.getRegionName()!=null &&!capital.getRegionName().equals("")){
                        str[2]=capital.getRegionName();
                     }
                    if(capital.getProvince()!=null &&!capital.getProvince().equals("")){
                        str[3]=capital.getProvince();
                     }
                    if(capital.getCity()!=null &&!capital.getCity().equals("")){
                        str[4]=capital.getCity();
                     }
                    if(capital.getCompany()!=null &&!capital.getCompany().equals("")){
                        str[5]=capital.getCompany();
                     }
                    if(capital.getAccountName()!=null && !capital.getAccountName().equals("")){
                       str[6]=capital.getAccountName();
                    }
                    if(capital.getAccountBank()!=null&&!capital.getAccountBank().equals("")){
                       str[7]=capital.getAccountBank();
                    }
                    if(capital.getAccount()!=null&&!capital.getAccount().equals("")){
                       str[8]=capital.getAccount();
                    }
                    if(capital.getAccountNature()!=null &&!capital.getAccountNature().equals("")){
                       str[9]=capital.getAccountNature();
                    }
                    if(capital.getTradeTime()!=null&&!capital.getTradeTime().equals("")){
                       str[10]=capital.getTradeTime();
                    }
                    if(capital.getStartBlack()!=null &&!capital.getStartBlack().equals("")){
                       str[11]=capital.getStartBlack().toString();
                    }
                    if(capital.getIncom()!=null &&!capital.getIncom().equals("")){
                       str[12]=capital.getIncom().toString();
                    }
                    if(capital.getPay()!=null&&!capital.getPay().equals("")){
                       str[13]=capital.getPay().toString();
                    }
                    if(capital.getEndBlack()!=null&&!capital.getEndBlack().equals("")){
                       str[14]=capital.getEndBlack().toString();
                    }
                    if(capital.getAbstrac()!=null&&!capital.getAbstrac().equals("")){
                        str[15]=capital.getAbstrac();
                    }
                    if(capital.getClassify()!=null&&!capital.getClassify().equals("")){
                        str[16]=capital.getClassify();
                    }
                    if(capital.getRemarks()!=null&&!capital.getRemarks().equals("")){
                        str[17]=capital.getRemarks();
                    } 
                        strList.add(str);
                }
                response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode("资金流水表", "UTF-8")+".xls");
                response.setContentType("application/octet-stream");
                os = response.getOutputStream();
                ExcelUtil.export(strList, os);
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            } catch (IOException e) {
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
                e.printStackTrace();
            } finally {
                if(os != null)
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        
}
