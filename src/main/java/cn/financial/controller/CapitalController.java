package cn.financial.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import cn.financial.model.Capital;
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
public class CapitalController {
 
    
        @Autowired
        private  CapitalService capitalService;
        
        @Autowired
        private OrganizationService organizationService;

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
        
        /***
         * 导入
         */
        @RequestMapping(value="/capital/excelImport",method=RequestMethod.POST)
        public void excelImport(MultipartFile uploadFile,HttpServletResponse response) throws IOException{
            Map<String, Object> dataMap = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(uploadFile.getSize()!= 0){  //判断文件大小是否为0
              try {
                int row=1;
                Integer a=0;
                List<String []> list=ExcelUtil.read(uploadFile.getInputStream(), row);
                for (int i = 0; i < list.size(); i++){
                    Capital capital=new Capital();
                    String[] str=list.get(i);
                    capital.setId(UuidUtil.getUUID());
                    
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
                    a = capitalService.insertCapital(capital);
                 }
                if (a == 1) {
                    dataMap.put("resultCode", 200);
                    dataMap.put("result", "导入成功!");
                } else {
                    dataMap.put("resultCode", 200);
                    dataMap.put("result", "导入失败!");
                }
            } catch (Exception e) {
                dataMap.put("resultCode", 500);
                dataMap.put("resultDesc", "服务器异常!");
                this.logger.error(e.getMessage(), e);
            }
        } 
      }
        
        
        /***
         * 导出
         * @param response
         * @throws Exception 
         */
        @RequestMapping(value="/capital/export")
        public void export(HttpServletRequest request,HttpServletResponse response,Map<Object, Object> map) throws Exception{
            OutputStream os = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                List<Capital> list = capitalService.listCapitalBy(map);
                List<String[]> strList=new ArrayList<>();
                String[] ss={"模板","事业部","大区名称","省份","城市","公司名称","户名","开户行","账户","账户性质",
                        "交易日期","期初余额","本期收入","本期支出","期末余额","摘要","项目分类","备注"};
                strList.add(ss);
                for (int i = 0; i < list.size(); i++) {
                    String[] str={};
                    str[6]=list.get(i).getAccountName();
                    str[7]=list.get(i).getAccountBank();
                    str[8]=list.get(i).getAccount();
                    str[9]=list.get(i).getAccountNature();
                    str[10]=sdf.format(list.get(i).getTradeTime());
                    str[11]=list.get(i).getStartBlack().toString();
                    str[12]=list.get(i).getIncom().toString();
                    str[13]=list.get(i).getPay().toString();
                    str[14]=list.get(i).getEndBlack().toString();
                    str[15]=list.get(i).getAbstrac();
                    str[16]=list.get(i).getClassify();
                    str[17]=list.get(i).getRemarks();
                    strList.add(str);
                }
                response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode("资金流水表", "UTF-8")+".xls");
                response.setContentType("application/octet-stream");
                os = response.getOutputStream();
                ExcelUtil.export(strList, os);
            } catch (IOException e) {
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
