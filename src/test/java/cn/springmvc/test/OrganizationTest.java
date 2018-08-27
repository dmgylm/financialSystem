package cn.springmvc.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;











import cn.financial.model.DataModule;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.model.response.ChildrenObject;
import cn.financial.model.response.OrangizaSubnode;
import cn.financial.model.response.OrganizaParnode;
import cn.financial.model.response.OrganizaResult;
import cn.financial.quartz.AccountQuartzListener;
import cn.financial.service.BusinessDataService;
import cn.financial.service.OrganizationService;
import cn.financial.service.impl.BusinessDataInfoServiceImpl;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.service.impl.DataModuleServiceImpl;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.service.impl.UserOrganizationServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.HttpClient3;
import cn.financial.util.JsonToHtmlUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml",
        "classpath:spring/spring-cache.xml", "classpath:spring/spring-shiro.xml", "classpath:spring/spring-redis.xml" })
public class OrganizationTest {

    @Autowired
    private OrganizationServiceImpl service;
    @Autowired
    private DataModuleServiceImpl datamodel;
    
    private HttpClient3 http = new HttpClient3();

    private OrganizationService organizationService;
    private OrganizationServiceImpl organizationServicess;
    /**
     * 测试获取xml的状态节点
     */
    //@Test
    public void XMLreturnValue() {
        HashMap<String, String> returnValue = ElementXMLUtils.returnValue("RUN_SUCCESSFULLY");
        System.out.println(returnValue.toString());
    }
    @Autowired
    private OrganizationServiceImpl oarimpl;
    private UserOrganizationServiceImpl userorganization;
    @Test
    public void orag(){
    	String id="801e3565e8ce40ecba5c8944eacea33d";
        List<Organization> lists =organizationServicess.listTreeByIdForParent(id);
        int parentOrgType=lists.get(1).getOrgType();
        System.out.println(lists.get(1).getOrgName());
    }
    @Test
    public void sarss(){
    	String id="801e3565e8ce40ecba5c8944eacea33d";
    	  Map<String,String> map=new HashMap<String, String>();
    	  List<Organization> result = service.listOrganizationBy(null,null,null,id,null,null,null,null,null);
    	  System.out.println(result.get(0).getOrgPlateId());
    }
    @Test
    public void getSubnode(){
    	  String id="7825dff0398841069ae41afd73f8677e";
          com.alibaba.fastjson.JSONObject  json=new com.alibaba.fastjson.JSONObject();
    	  json= oarimpl.TreeByIdForSon(id);
          com.alibaba.fastjson.JSONArray jsons=com.alibaba.fastjson.JSONArray.parseArray(json.get("children").toString());
          if(jsons.size()==0){
        	  if(Integer.parseInt(json.get("orgType").toString())==3){
        		  System.out.println(json.get("pid"));
        	  }
          }
          else{
        	  for(int i=0;i<jsons.size();i++){
        		  com.alibaba.fastjson.JSONObject jsonss=com.alibaba.fastjson.JSONObject.parseObject(jsons.get(i).toString());
            	  if(Integer.parseInt(jsonss.get("orgType").toString())==3){
            	  System.out.println(jsonss.get("pid"));
            	  }
              }
          }
         

          
          
    }
    @Test
    public void ss(){
	   List<String> ids = new ArrayList<String>();
	   ids.add("000791b90fc448ae9a2da761c5e281ef");
	   ids.add("000e2e05c8f145578055cb5715c7c605");
//	   Map<String, String> map = (Map<String, String>) datamodel.dataModuleById(null,ids);
//	   System.out.println(map);
    }
    @Test
    public void part(){
    	 List<Organization> list = null;
    	 String id="0a374526c54c410e8ebd449fddc5c75c";
    	  list = service.listTreeByIdForParent(id);
    	  int sa=0;
    	  for(int i=0;i<list.size();i++){
     		 int z=list.get(i).getOrgType();
     		 if(z==2){
     			 sa++;
     		 }
     	  }
         System.out.println(sa);
    	 
    	  
    	 
    }
    @Test
    public void ssa(){
    	String id="34be1433b4be4be3978e9910fca56106";
    	 List<Organization> list = service.listOrganizationBy(null,null,null,id,null,null,null,null,null);
    	System.out.println(list.get(0).getOrgType());
    }
    /**
     * 新增组织结构
     * @throws UnsupportedEncodingException 
     */
    //@Test
    public void saveOrganization() {
        http = new HttpClient3();
        try {
            String string = http.doPost(
                    "http://192.168.111.162:8083/financialSystem/organization/save?meta.session.id=e8ea950b-1fd3-411c-a85b-3bf5358195b6",
                    "orgName=测试&orgType=3&parentOrgId=5d9f44219a3a47aaa2a4deae332d1cb8");
            System.out.println(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有的组织结构信息
     */
    //@Test
    public void listOrganization() {
        long start = System.currentTimeMillis();
        http = new HttpClient3();
        String result = "";
        try {
            result = http.doPost(
                    "http://192.168.111.162:8083/financialSys/organization/listBy?meta.session.id=e8ea950b-1fd3-411c-a85b-3bf5358195b6",
                    "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("花费时间:" + (end - start));
    }
    @Autowired
    private OrganizationService services;
  @Test 
  public void sss(){
	  List<Organization> ls=services.listOrganizationBy("盛世大联保险代理股份有限公司内蒙古分公司",null,null,null,null, "404ed3a5442c4ed78331d6c77077958f",null,2,null);
	  System.out.println("展示数据"+ls.size());
  }
    /**
     * 根据传入的map查询相应的组织结构
     */
    //@Test
    public void listOrganizationBy() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "0994b84cd9e14a2cb706b53708e67f9b");
        // map.put("code", "01");
        // map.put("orgName", "总裁");
        // map.put("parentId", "0");
        //map.put("createTime", "2018-03-23");
        // map.put("updateTime", "");
        // map.put("uId", "1cb54fff435b4fff8aa7c1fa391f519b");
        // map.put("his_permission", "");
        long start = System.currentTimeMillis();
        http = new HttpClient3();
        String result = "";
        try {
            result = http.doPost(
                    "http://192.168.111.162:8083/financialSys/organization/listBy?meta.session.id=e8ea950b-1fd3-411c-a85b-3bf5358195b6",
                    map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("花费时间:" + (end - start));
    }

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     */
    //@Test
    public void updateOrganizationById() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "181b407f574b4f1588aa4ad91a23a582");
        map.put("orgName", "测测测测vcshishi");
        long start = System.currentTimeMillis();
        http = new HttpClient3();
        String result = "";
        try {
            result = http.doPost(
                    "http://192.168.111.162:8083/financialSys/organization/updatebyid?meta.session.id=e8ea950b-1fd3-411c-a85b-3bf5358195b6",
                    map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("花费时间:" + (end - start));
    }

    /**
     * 停用(先判断此节点下是否存在未停用的子节点，若存在，则返回先删除子节点;否则继续停用此节点)
     */
    //@Test
    public void deleteOrganizationByStatus() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", "181b407f574b4f1588aa4ad91a23a582");
        long start = System.currentTimeMillis();
        http = new HttpClient3();
        String result = "";
        try {
            result = http.doPost(
                    "http://192.168.111.162:8083/financialSys/organization/discontinuate?meta.session.id=e8ea950b-1fd3-411c-a85b-3bf5358195b6",
                    map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("花费时间:" + (end - start));
    }

    /**
     * 根据id查询该节点下的所有子节点 ,构建成树
     */
    @Test
    public void TreeByOrgId() {
        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("id", "00a1f4a699c945638e4c6114e9a8448d");
        long start = System.currentTimeMillis();
        http = new HttpClient3();
        String result = "";
        try {
            result = http.doPost(
                    "http://localhost:8083/financialSystem/organization/getSubnode?meta.session.id=8ada939f-4536-4d92-b3d3-244fd94fdb4a",
                    map);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("花费时间:" + (end - start));
    }
 
    /**
     * 查询该节点以及节点以下的
     */
    @Test
//    public void orgshow() {
//        try {
//            List<String> ids = new ArrayList<String>();
//            ids.add("f946a081eda949228537a8746200c3d6");
//            ids.add("cced74c59a9846b5b0a81c0baf235c17");
//            ids.add("f8483e1c85e84323853aeee27b4e8c91");
//            ids.add("e71064dc0fc443fa8893ce489aed8c38");
//            List<Organization> list = service.listOrganization(ids);
//            List<String> listmap = new ArrayList<String>();
//            for (Organization organization : list) {
//                String his_permission = organization.getHis_permission();
//                String[] hps = his_permission.split(",");// 分割逗号
//                listmap.addAll(Arrays.asList(hps));// 所有的his_permission存到listmap当中
//            }
//            JSONObject obj = new JSONObject();
//            // 查询对应的节点的数据
//            List<Organization> listshow = service.listOrganizationcode(listmap);
//            JSONArray json = JSONArray.fromObject(listshow);
//            try {
//                obj.put("rows", json);
//                obj.put("resultCode", 200);
//            } catch (Exception e) {
//                obj.put("resultCode", 500);
//                obj.put("resultDesc", "服务器异常");
//            }
//            System.out.println(json);
///*            for (Organization organization : listshow) {
//                System.out.println(organization.toString());
//            }
//            System.out.println("展示" + listmap);*/
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    public void orgorganiza() {

            List<String> ids = new ArrayList<String>();
            ids.add("f946a081eda949228537a8746200c3d6");
            ids.add("cced74c59a9846b5b0a81c0baf235c17");
            ids.add("f8483e1c85e84323853aeee27b4e8c91");
            ids.add("e71064dc0fc443fa8893ce489aed8c38");
            List<Organization> list=service.listOrganization(ids);
            JSONArray json=JSONArray.fromObject(list);
            System.out.println(json.size());
        }
 
    /**
     * 根据id查询该节点下的所有子节点 的集合
     */
    @Test
    public void listTreeByOrgId() {                           
        List<Organization> list = service.listTreeByIdForSon("cced74c59a9846b5b0a81c0baf235c17");
        for (Organization organization : list) {
            System.out.println(organization.toString());
        }
        System.out.println(list.size());
    }
    @Test
    public void sar() {
      com.alibaba.fastjson.JSONObject json= service.TreeByIdForSon("");
      System.out.println(json);
       
    }

    /**
     * 根据id查询该节点下的所有父节点
     */
    //@Test
    public void listTreeByOrgIdParent() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", "2a79258d80844960ba32e8e77b1c4b88");
        long start = System.currentTimeMillis();
        http = new HttpClient3();
        String result = "";
        try {
            result = http.doPost(
                    "http://192.168.111.162:8083/financialSys/organization/getparnode?meta.session.id=e8ea950b-1fd3-411c-a85b-3bf5358195b6",
                    map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("花费时间:" + (end - start));
    }
    @Test
    public void organizamove(){
    	User user=new User();
    	service.moveOrganization(user, "03a8b64983c64adc8e9bf198917748b8", "");
    }
    /**
     * 移动组织机构
     * 
     * @throws Exception
     */
    @Test
    public void moveOrganization() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", "181b407f574b4f1588aa4ad91a23a582");
        map.put("parentId", "2a79258d80844960ba32e8e77b1c4b88");
        long start = System.currentTimeMillis();
        http = new HttpClient3();
        String result = "";
        try {
            result = http.doPost(
                    "http://192.168.111.162:8083/financialSys/organization/move?meta.session.id=e8ea950b-1fd3-411c-a85b-3bf5358195b6",
                    map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("花费时间:" + (end - start));
    }

    /**
     * 根据条件判断是否该节点存在子节点
     */
    //@Test
    public void hasOrganizationSon() {
        HashMap<String, String> map = new HashMap<String, String>();
        // map.put("id", "5d9f44219a3a47aaa2a4deae332d1cb8");
        map.put("orgName", "测试");
        long start = System.currentTimeMillis();
        http = new HttpClient3();
        String result = "";
        try {
            result = http.doPost(
                    "http://192.168.111.162:8083/financialSys/organization/hasSon?meta.session.id=e8ea950b-1fd3-411c-a85b-3bf5358195b6",
                    map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("花费时间:" + (end - start));
    }

    /**
     * 获取所有公司
     */
    @Test
    public void getCompany() {
        List<Organization> list = service.getCompany();
        System.out.println(JSONArray.fromObject(list).toString());
    }

    /**
     * 根据公司以下节点的id，查询到该节点所属公司
     */
    //@Test
    public void getCompanyNameBySon() {
        Organization organization = service.getCompanyNameBySon("06e37893beb249b7a3a34eb4cc61d7ef");
        if (null != organization) {
            System.out.println(organization.toString());
        }
    }

    /**
     * 获取所有部门
     */
    //@Test
    public void getDep() {
        long start1 = System.currentTimeMillis();
        List<Organization> re = service.getDep();
        long end1 = System.currentTimeMillis();
        System.out.println(JSONArray.fromObject(re).toString());
        // System.out.println(end1 - start1);
        // System.out.println(re.size());
    }

    /**
     * 根据某个节点，查询到父级的某个节点
     */
    @Test
    public void getOrgaUpFromOne() {
        long start = System.currentTimeMillis();
        Organization dep = service.getOrgaUpFromOne("e9f092f70b2e423b8cd9598eadf9077e",
                "94a24264738f42d6ac52d7e8806ba345");
        JSONObject fromObject = JSONObject.fromObject(dep);
        System.out.println(fromObject);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        // System.out.println(dep.toString());
    }
    
    /**
     * 模拟根据json字符串生成html的table标签，方法一
     */
    //@Test
    public void toHtml() {
        String jsonOfStr = "{\"biaoming\":null,\"gongsiming\":null,"
                + "\"yewu\":null,\"bumen\":null,\"year\":null,\"month\":null,"
                + "\"data\":{\"bxyye\":{\"heji\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null},\"jiaoqiangxian\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null},\"shangyexian\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null}},\"bxyw\":{\"shouru\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null},\"chengben\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null},\"rengongchengben\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null," + "\"wanchenglv\":null}}}}";
        String jj = "{\"biaoming\":null,\"gongsiming\":null,\"yewu\":null,\"bumen\":null,"
                + "\"year\":null,\"month\":null,\"data\":{\"aaaaaa\":{\"bxyye\":{"
                + "\"heji\":{\"benyueshiji\":null,\"benyuebudian\":null,"
                + "\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},"
                + "\"jiaoqiangxian\":{\"benyueshiji\":null,\"benyuebudian\":null,"
                + "\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},"
                + "\"shangyexian\":{\"benyueshiji\":null,\"benyuebudian\":null,"
                + "\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}},"
                + "\"bxyw\":{\"shouru\":{\"benyueshiji\":null,\"benyuebudian\":null,"
                + "\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},"
                + "\"chengben\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,"
                + "\"benyueyusuan\":null,\"wanchenglv\":null},\"rengongchengben\":{"
                + "\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,"
                + "\"benyueyusuan\":null,\"wanchenglv\":null}}}}}";
        String hh = "{\"biaoming\":null,\"gongsiming\":null,\"yewu\":null,\"bumen\":null,\"year\":null,\"month\":null,\"data\":{\"aaaaaa\":{\"bxyye\":{\"heji\":{\"aa\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}},\"jiaoqiangxian\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"shangyexian\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}},\"bxyw\":{\"shouru\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"chengben\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"rengongchengben\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}}}}}";
        JSONObject jsonObject1 = JSONObject.fromObject(hh);
        JSONObject data = JSONObject.fromObject(jsonObject1.get("data").toString());
        String result = "<table border=\"1\">";
        List<String> listtd = new ArrayList<>();
        List<String> listtr = new ArrayList<>();
        List<JSONObject> listtr1 = new ArrayList<>();
        String rStr = JsonToHtmlUtil.jsonToTable(data, listtd, listtr, listtr1);

        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> listtd1 = new ArrayList<>();
        JsonToHtmlUtil.JsonToMap(data, map, listtd1);
        JsonToHtmlUtil.iteration(JsonToHtmlUtil.sortMap(map));
        String dStr = "<tr><td colspan=\'" + JsonToHtmlUtil.index + "\'>项目</td>";

        for (String string : listtd) {
            dStr += ("<td>" + string + "</td>");
        }
        dStr += "</tr>";
        result = result + dStr + rStr + "</table>";
        for (int i = 0; i < listtr1.size(); i++) {
            List<String> listend = new ArrayList<>();
            JsonToHtmlUtil.getNum(listtr1.get(i), listend);
            int rowspan = listend.size() + 1;
            if (rowspan > 1) {
                result = result.replace("<td>" + listtr.get(i).toString() + "</td>",
                        "<td rowspan=\"" + rowspan + "\">" + listtr.get(i).toString() + "</td>");
            } else {
                String td = "";
                for (int j = 0; j < listtd.size(); j++) {
                    td += "<td></td>";
                }
                result = result.replace("<td>" + listtr.get(i).toString() + "</td>",
                        "<td rowspan=\"" + rowspan + "\">" + listtr.get(i).toString() + "</td>" + td);
            }
        }
        System.out.println(result);
    }

    /**
     * 模拟根据json字符串生成html的table标签,方法二
     */
    //@Test
    public void toHtml2() {
        String jsonOfStr = "{\"biaoming\":null,\"gongsiming\":null,"
                + "\"yewu\":null,\"bumen\":null,\"year\":null,\"month\":null,"
                + "\"data\":{\"bxyye\":{\"heji\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null},\"jiaoqiangxian\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null},\"shangyexian\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null}},\"bxyw\":{\"shouru\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null},\"chengben\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
                + "\"wanchenglv\":null},\"rengongchengben\":{\"benyueshiji\":null,"
                + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}}}}";

        String jj = "{\"biaoming\":null,\"gongsiming\":null,\"yewu\":null,\"bumen\":null,"
                + "\"year\":null,\"month\":null,\"data\":{\"aaaaaa\":{\"bxyye\":{"
                + "\"heji\":{\"benyueshiji\":null,\"benyuebudian\":null,"
                + "\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},"
                + "\"jiaoqiangxian\":{\"benyueshiji\":null,\"benyuebudian\":null,"
                + "\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},"
                + "\"shangyexian\":{\"benyueshiji\":null,\"benyuebudian\":null,"
                + "\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}},"
                + "\"bxyw\":{\"shouru\":{\"benyueshiji\":null,\"benyuebudian\":null,"
                + "\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},"
                + "\"chengben\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,"
                + "\"benyueyusuan\":null,\"wanchenglv\":null},\"rengongchengben\":{"
                + "\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,"
                + "\"benyueyusuan\":null,\"wanchenglv\":null}}}}}";

        String hh = "{\"biaoming\":null,\"gongsiming\":null,\"yewu\":null,\"bumen\":null,\"year\":null,\"month\":null,\"data\":{\"aaaaaa\":{\"bxyye\":{\"heji\":{\"aa\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}},\"jiaoqiangxian\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"shangyexian\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}},\"bxyw\":{\"shouru\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"chengben\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"rengongchengben\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}}}}}";

        JSONObject jsonObject = JSONObject.fromObject(hh);
        JSONObject data = JSONObject.fromObject(jsonObject.get("data").toString());
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<String> listtd = new ArrayList<>();
        JsonToHtmlUtil.JsonToMap(data, map, listtd);

        String result = "";
        String start = "<table border='1'>";
        String ite = JsonToHtmlUtil.iteration(JsonToHtmlUtil.sortMap(map));
        String end = "</table>";
        String he = "<tr><td colspan=\'" + JsonToHtmlUtil.index + "\'>项目</td>";
        for (String s : listtd) {
            he += "<td>" + s + "</td>";
        }
        he += "</tr>";
        result = start + he + ite + end;

        List<String> listtr = new ArrayList<>();
        List<JSONObject> listtr1 = new ArrayList<>();
        JsonToHtmlUtil.JsonTr(data, listtr, listtr1);
        for (int i = 0; i < listtr1.size(); i++) {
            List<String> listend = new ArrayList<>();
            JsonToHtmlUtil.getNum(listtr1.get(i), listend);
            int rowspan = listend.size() + 1;
            if (rowspan > 1) {
                result = result.replace("<td>" + listtr.get(i).toString() + "</td>",
                        "<td rowspan=\"" + rowspan + "\">" + listtr.get(i).toString() + "</td>");
            } else {
                result = result.replace("<td>" + listtr.get(i).toString() + "</td>",
                        "<td rowspan=\"" + rowspan + "\">" + listtr.get(i).toString() + "</td>");
            }
        }

        System.out.println(result);
    }

}
