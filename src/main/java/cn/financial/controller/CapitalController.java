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
import cn.financial.util.ExcelUtil;
import cn.financial.util.UuidUtil;

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
        
        @RequestMapping(value="/excel", method = RequestMethod.POST)
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
                if(request.getParameter("id")!=null && !request.getParameter("id").equals("")){
                   map.put("id", request.getParameter("id"));
                }
                if(request.getParameter("plate")!=null && !request.getParameter("plate").equals("")){
                    map.put("plate",new String(request.getParameter("plate").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("BU")!=null && !request.getParameter("BU").equals("")){
                    map.put("BU",new String(request.getParameter("BU").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("regionName")!=null && !request.getParameter("regionName").equals("")){
                    map.put("regionName",new String(request.getParameter("regionName").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("province")!=null && !request.getParameter("province").equals("")){
                    map.put("province",new String(request.getParameter("province").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("city")!=null && !request.getParameter("city").equals("")){
                    map.put("city",new String(request.getParameter("city").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("company")!=null && !request.getParameter("company").equals("")){
                    map.put("company",new String(request.getParameter("company").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountName")!=null && !request.getParameter("accountName").equals("")){
                    map.put("accountName",new String(request.getParameter("accountName").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountBank")!=null && !request.getParameter("accountBank").equals("")){
                    map.put("accountBank",new String(request.getParameter("accountBank").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("account")!=null && !request.getParameter("account").equals("")){
                    map.put("account",new String(request.getParameter("account").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("accountNature")!=null && !request.getParameter("accountNature").equals("")){
                    map.put("accountNature",new String(request.getParameter("accountNature").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("tradeTime")!=null && !request.getParameter("tradeTime").equals("")){
                    map.put("tradeTime",request.getParameter("tradeTime"));
                }
                if(request.getParameter("startBlack")!=null && !request.getParameter("startBlack").equals("")){
                    map.put("startBlack",Integer.getInteger(request.getParameter("startBlack")));
                }
                if(request.getParameter("incom")!=null && !request.getParameter("incom").equals("")){
                    map.put("incom",Integer.getInteger(request.getParameter("incom")));
                }
                if(request.getParameter("pay")!=null && !request.getParameter("pay").equals("")){
                    map.put("pay",Integer.getInteger(request.getParameter("pay")));
                }
                if(request.getParameter("endBlack")!=null && !request.getParameter("endBlack").equals("")){
                    map.put("endBlack",Integer.getInteger(request.getParameter("endBlack")));
                }
                if(request.getParameter("abstrac")!=null && !request.getParameter("abstrac").equals("")){
                    map.put("abstrac",new String(request.getParameter("abstrac").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("classify")!=null && !request.getParameter("classify").equals("")){
                    map.put("classify",new String(request.getParameter("classify").getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(request.getParameter("createTime")!=null && !request.getParameter("createTime").equals("")){
                    map.put("createTime",request.getParameter("createTime"));
                }
                if(request.getParameter("updateTime")!=null && !request.getParameter("updateTime").equals("")){
                    map.put("updateTime",request.getParameter("updateTime"));
                }
                if(uId!=null && !uId.equals("")){
                    map.put("uId",uId);
                }
                if(request.getParameter("remarks")!=null && !request.getParameter("remarks").equals("")){
                    map.put("remarks",new String(request.getParameter("remarks").getBytes("ISO-8859-1"), "UTF-8"));
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
         * 根据id查询资金数据
         * 
         * @param request
         * @param id
         *           
         * @return
         */
        @RequiresPermissions("resource:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> selectCapitalById(HttpServletRequest request, String id) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                if(id!=null&&!id.equals("")){
                   Capital  Capital=capitalService.selectCapitalById(id);
                   dataMap.put("resultCode", 200);
                   dataMap.put("resultDesc", "查询成功!");
                   dataMap.put("resultData", Capital);
                }
            } catch (Exception e) {
                dataMap.put("resultCode", 400);
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
        @RequiresPermissions("capital:create")
        @RequestMapping(value="/insert", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> insertCapital(HttpServletRequest request,HttpServletResponse response,String plate,String BU,
            String regionName,String province,String city,String company,String accountName,String accountBank,String account,
            String accountNature,String tradeTime,String startBlack,String incom,String pay,String endBlack,String abstrac,
            String classify,String year,String month,String remarks){
            Map<String, Object> dataMap = new HashMap<String, Object>();
           try {
               User user = (User) request.getAttribute("user");
               String uId = user.getId();
               Capital capital =new Capital();
               capital.setId(UuidUtil.getUUID());
               if(plate!=null && !plate.equals("")){
                   capital.setPlate(new String(plate.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(BU!=null && !BU.equals("")){
                   capital.setBU(new String(BU.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(regionName!=null && !regionName.equals("")){
                   capital.setRegionName(new String(regionName.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(province!=null && !province.equals("")){
                   capital.setProvince(new String(province.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(city!=null && !city.equals("")){
                   capital.setCity(new String(city.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(company!=null && !company.equals("")){
                   capital.setCompany(new String(company.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(accountName!=null && !accountName.equals("")){
                   capital.setAccountName(new String(accountName.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(accountBank!=null && !accountBank.equals("")){
                   capital.setAccountBank(new String(accountBank.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(account!=null && !account.equals("")){
                   capital.setAccount(new String(account.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(accountNature!=null && !accountNature.equals("")){
                   capital.setAccountNature(new String(accountNature.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(tradeTime!=null && !tradeTime.equals("")){
                   capital.setTradeTime(tradeTime);
               }
               if(startBlack!=null && !startBlack.equals("")){
                   capital.setStartBlack(Integer.getInteger(startBlack));
               }
               if(incom!=null && !incom.equals("")){
                   capital.setIncom(Integer.getInteger(incom));
               }
               if(pay!=null && !pay.equals("")){
                   capital.setPay(Integer.getInteger(pay));
               }
               if(endBlack!=null && !endBlack.equals("")){
                   capital.setEndBlack(Integer.getInteger(endBlack));
               }
               if(abstrac!=null && !abstrac.equals("")){
                   capital.setAbstrac(new String(abstrac.getBytes("ISO-8859-1"), "UTF-8"));
               }
               if(classify!=null && !classify.equals("")){
                   capital.setClassify(new String(classify.getBytes("ISO-8859-1"), "UTF-8"));
               }
             
               if(uId!=null && !uId.equals("")){
                   capital.setuId(uId);
               }
               if(year!=null && !year.equals("")){
                   capital.setYear(Integer.getInteger(year));
               }
               if(month!=null && !month.equals("")){
                   capital.setMonth(Integer.getInteger(month));
               }
               if(remarks!=null && !remarks.equals("")){
                   capital.setRemarks(new String(remarks.getBytes("ISO-8859-1"), "UTF-8"));
               }
               capital.setStatus(1);
               Integer i = capitalService.insertCapital(capital);
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
         * 修改资金数据
         * @param request
         * @return
         */
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
        @ResponseBody
        public Map<String, Object> updateCapital(HttpServletRequest request,String id,String plate,String BU,
                String regionName,String province,String city,String company,String accountName,String accountBank,String account,
                String accountNature,String tradeTime,String startBlack,String incom,String pay,String endBlack,String abstrac,
                String classify,String year,String month,String remarks) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                Capital capital =new Capital();
                if(id!=null && !id.equals("")){
                    capital.setId(id);
                }
                if(plate!=null && !plate.equals("")){
                    capital.setPlate(new String(plate.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(BU!=null && !BU.equals("")){
                    capital.setBU(new String(BU.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(regionName!=null && !regionName.equals("")){
                    capital.setRegionName(new String(regionName.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(province!=null && !province.equals("")){
                    capital.setProvince(new String(province.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(city!=null && !city.equals("")){
                    capital.setCity(new String(city.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(company!=null && !company.equals("")){
                    capital.setCompany(new String(company.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(accountName!=null && !accountName.equals("")){
                    capital.setAccountName(new String(accountName.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(accountBank!=null && !accountBank.equals("")){
                    capital.setAccountBank(new String(accountBank.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(account!=null && !account.equals("")){
                    capital.setAccount(new String(account.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(accountNature!=null && !accountNature.equals("")){
                    capital.setAccountNature(new String(accountNature.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(tradeTime!=null && !tradeTime.equals("")){
                    capital.setTradeTime(tradeTime);
                }
                if(startBlack!=null && !startBlack.equals("")){
                    capital.setStartBlack(Integer.getInteger(startBlack));
                }
                if(incom!=null && !incom.equals("")){
                    capital.setIncom(Integer.getInteger(incom));
                }
                if(pay!=null && !pay.equals("")){
                    capital.setPay(Integer.getInteger(pay));
                }
                if(endBlack!=null && !endBlack.equals("")){
                    capital.setEndBlack(Integer.getInteger(endBlack));
                }
                if(abstrac!=null && !abstrac.equals("")){
                    capital.setAbstrac(new String(abstrac.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(classify!=null && !classify.equals("")){
                    capital.setClassify(new String(classify.getBytes("ISO-8859-1"), "UTF-8"));
                }
                if(uId!=null && !uId.equals("")){
                    capital.setuId(uId);
                }
                if(year!=null && !year.equals("")){
                    capital.setYear(Integer.getInteger(year));
                }
                if(month!=null && !month.equals("")){
                    capital.setMonth(Integer.getInteger(month));
                }
                if(remarks!=null && !remarks.equals("")){
                    capital.setRemarks(new String(remarks.getBytes("ISO-8859-1"), "UTF-8"));
                }
                capital.setStatus(1);
                Integer i = capitalService.updateCapital(capital);
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
                    dataMap.put("resultCode", 200);
                    dataMap.put("resultDesc", "删除成功!");
                 } else {
                    dataMap.put("resultCode", 400);
                    dataMap.put("resultDesc", "删除失败!");
                 }     
                }
            } catch (Exception e) {
                dataMap.put("resultCode", 500);
                dataMap.put("resultDesc", "服务器异常!");
                this.logger.error(e.getMessage(), e);
            }
            return dataMap;
        }
        
       
        /***
         * 导入
         */
        @RequiresPermissions("capital:upload")
        @RequestMapping(value="/excelImport",method = RequestMethod.POST)
        @ResponseBody
        public void excelImport(MultipartFile uploadFile,HttpServletRequest request) throws IOException{
            Map<String, Object> dataMap = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(uploadFile.getSize()>0 && uploadFile.getSize()<5242880){  //判断文件大小是否是5M以下的
              try {
                int row=1;
                Integer a=0;
                List<String []> list=ExcelUtil.read(uploadFile.getInputStream(), row);
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
                    a = capitalService.insertCapital(capital);
                 }
                if (a == 1) {
                    dataMap.put("resultCode", 200);
                    dataMap.put("result", "导入成功!");
                } else {
                    dataMap.put("resultCode", 400);
                    dataMap.put("result", "导入失败!");
                }
            } catch (Exception e) {
                dataMap.put("resultCode", 500);
                dataMap.put("resultDesc", "服务器异常!");
                this.logger.error(e.getMessage(), e);
            }
        }else{
            dataMap.put("resultCode", 400);
            dataMap.put("result", "文件大于5M!请重新上传");
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
           /* User user = (User) request.getAttribute("user");
            uId = user.getId();*/
          /*  Map<Object, Object> map = new HashMap<>();
            if(id!=null&&!id.equals("")){
                map.put("id",id);
            }*/
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
        }
        
}
