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
import cn.financial.service.OrganizationService;
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


        protected Logger logger = LoggerFactory.getLogger(OrganizationController.class);
        
        @RequestMapping(value="/excel", method = RequestMethod.GET)
        public String getexcel(HttpServletRequest request, HttpServletResponse response) {
            return "cel";
        }

      
        /**
         * 查询所有的资金数据
         * 
         * @param request
         * @param response
         */
        @RequiresPermissions("capital:view")
        @RequestMapping(value="/list", method = RequestMethod.POST)
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
        @RequiresPermissions("capital:view")
        @RequestMapping(value="/listBy", method = RequestMethod.POST)
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
        @RequiresPermissions("resource:view")
        @RequestMapping(value="/listById", method = RequestMethod.POST)
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
        @RequiresPermissions("capital:create")
        @RequestMapping(value="/insert", method = RequestMethod.POST)
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
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/update", method = RequestMethod.POST)
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
        @RequiresPermissions("capital:update")
        @RequestMapping(value="/delete", method = RequestMethod.POST)
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<Object, Object> map =new HashMap<Object, Object>();
            User user = (User) request.getAttribute("user");
            uId = user.getId();
            if(uId!=null&&!uId.equals("")){
                map.put("uId", uId);
            }
            if(accountName!=null&&!accountName.equals("")){
                map.put("accountName",accountName);
            }
            if(accountNature!=null&&!accountNature.equals("")){
                map.put("accountNature",accountNature);
            }
            if(classify!=null&&!classify.equals("")){
                map.put("classify",classify);
            }
            try {
                List<Capital> list = capitalService.listCapitalBy(map);
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
