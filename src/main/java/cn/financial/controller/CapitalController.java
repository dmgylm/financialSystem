package cn.financial.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        public Map<String, Object> listCapitalBy(HttpServletRequest request,String id,String plate,String BU,
                String regionName,String province,String city,String company,String accountName,String accountBank,String account,
                String accountNature,String tradeTime,String startBlack,String incom,String pay,String endBlack,String abstrac,
                String classify,String createTime,String updateTime,String year,String month,String remarks,String status) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                Capital capital=new Capital();
                if(id!=null && !id.equals("")){
                   capital.setId(id);
                }
                if(plate!=null && !plate.equals("")){
                    capital.setPlate(plate);
                }
                if(BU!=null && !BU.equals("")){
                    capital.setBU(BU);
                }
                if(regionName!=null && !regionName.equals("")){
                    capital.setRegionName(regionName);
                }
                if(province!=null && !province.equals("")){
                    capital.setProvince(province);
                }
                if(city!=null && !city.equals("")){
                    capital.setCity(city);
                }
                if(company!=null && !company.equals("")){
                    capital.setCompany(company);
                }
                if(accountName!=null && !accountName.equals("")){
                    capital.setAccountName(accountName);
                }
                if(accountBank!=null && !accountBank.equals("")){
                    capital.setAccountBank(accountBank);
                }
                if(account!=null && !account.equals("")){
                    capital.setAccount(account);
                }
                if(accountNature!=null && !accountNature.equals("")){
                    capital.setAccountNature(accountNature);
                }
                if(tradeTime!=null && !tradeTime.equals("")){
                    capital.setTradeTime(sdf.parse(tradeTime));
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
                    capital.setAbstrac(abstrac);
                }
                if(classify!=null && !classify.equals("")){
                    capital.setClassify(classify);
                }
                if(createTime!=null && !createTime.equals("")){
                    capital.setCreateTime(sdf.parse(createTime));
                }
                if(updateTime!=null && !updateTime.equals("")){
                    capital.setUpdateTime(sdf.parse(updateTime));
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
                    capital.setRemarks(remarks);
                }
                if(status!=null && !status.equals("")){
                   capital.setStatus(Integer.getInteger(status));
                }
                List<Capital> list = capitalService.listCapitalBy(capital);
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
        @RequiresPermissions("resource:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
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
        @RequiresPermissions("capital:create")
        @RequestMapping(value="/insert", method = RequestMethod.POST)
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
                   capital.setPlate(plate);
               }
               if(BU!=null && !BU.equals("")){
                   capital.setBU(BU);
               }
               if(regionName!=null && !regionName.equals("")){
                   capital.setRegionName(regionName);
               }
               if(province!=null && !province.equals("")){
                   capital.setProvince(province);
               }
               if(city!=null && !city.equals("")){
                   capital.setCity(city);
               }
               if(company!=null && !company.equals("")){
                   capital.setCompany(company);
               }
               if(accountName!=null && !accountName.equals("")){
                   capital.setAccountName(accountName);
               }
               if(accountBank!=null && !accountBank.equals("")){
                   capital.setAccountBank(accountBank);
               }
               if(account!=null && !account.equals("")){
                   capital.setAccount(account);
               }
               if(accountNature!=null && !accountNature.equals("")){
                   capital.setAccountNature(accountNature);
               }
               if(tradeTime!=null && !tradeTime.equals("")){
                   capital.setTradeTime(sdf.parse(tradeTime));
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
                   capital.setAbstrac(abstrac);
               }
               if(classify!=null && !classify.equals("")){
                   capital.setClassify(classify);
               }
                   capital.setCreateTime(new Date());
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
                   capital.setRemarks(remarks);
               }
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
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
        public Map<String, Object> updateCapital(HttpServletRequest request,String id,String plate,String BU,
                String regionName,String province,String city,String company,String accountName,String accountBank,String account,
                String accountNature,String tradeTime,String startBlack,String incom,String pay,String endBlack,String abstrac,
                String classify,String year,String month,String remarks) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                User user = (User) request.getAttribute("user");
                String uId = user.getId();
                Capital capital =new Capital();
                capital.setId(id);
                if(plate!=null && !plate.equals("")){
                    capital.setPlate(plate);
                }
                if(BU!=null && !BU.equals("")){
                    capital.setBU(BU);
                }
                if(regionName!=null && !regionName.equals("")){
                    capital.setRegionName(regionName);
                }
                if(province!=null && !province.equals("")){
                    capital.setProvince(province);
                }
                if(city!=null && !city.equals("")){
                    capital.setCity(city);
                }
                if(company!=null && !company.equals("")){
                    capital.setCompany(company);
                }
                if(accountName!=null && !accountName.equals("")){
                    capital.setAccountName(accountName);
                }
                if(accountBank!=null && !accountBank.equals("")){
                    capital.setAccountBank(accountBank);
                }
                if(account!=null && !account.equals("")){
                    capital.setAccount(account);
                }
                if(accountNature!=null && !accountNature.equals("")){
                    capital.setAccountNature(accountNature);
                }
                if(tradeTime!=null && !tradeTime.equals("")){
                    capital.setTradeTime(sdf.parse(tradeTime));
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
                    capital.setAbstrac(abstrac);
                }
                if(classify!=null && !classify.equals("")){
                    capital.setClassify(classify);
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
                    capital.setRemarks(remarks);
                }
                capital.setUpdateTime(new Date());
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
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/delete", method = RequestMethod.POST)
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
                    dataMap.put("resultCode", 200);
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
        @RequestMapping(value="/excelImport",method = RequestMethod.POST)
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
                    capital.setTradeTime(sdf.parse(str[10]));
                    capital.setStartBlack(Integer.parseInt(str[11]));
                    capital.setIncom(Integer.parseInt(str[12]));
                    capital.setPay(Integer.parseInt(str[13]));
                    capital.setEndBlack(Integer.parseInt(str[14]));
                    capital.setAbstrac(str[15]);
                    capital.setClassify(str[16]);
                    capital.setRemarks(str[17]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(calendar.getTime());
                    capital.setCreateTime(new Date());
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
        @RequestMapping(value="/export",method = RequestMethod.POST)
        public void export(HttpServletRequest request,HttpServletResponse response,String uId,String accountName
                ,String accountNature,String classify) throws Exception{
            OutputStream os = null;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            User user = (User) request.getAttribute("user");
            uId = user.getId();
            Capital cap=new Capital();
            if(uId!=null&&!uId.equals("")){
                cap.setuId(uId);
            }
            if(accountName!=null&&!accountName.equals("")){
                cap.setAccountName(accountName);
            }
            if(accountNature!=null&&!accountNature.equals("")){
                cap.setAccountNature(accountNature);
            }
            if(classify!=null&&!classify.equals("")){
                cap.setClassify(classify);
            }
            try {
                List<Capital> list = capitalService.listCapitalBy(cap);
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
                       str[10]=sdf.format(capital.getTradeTime());
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
