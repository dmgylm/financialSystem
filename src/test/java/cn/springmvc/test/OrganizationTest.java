package cn.springmvc.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.financial.model.Organization;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.JsonToHtmlUtil;
import cn.financial.util.UuidUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml",
        "classpath:spring/spring-cache.xml", "classpath:spring/spring-shiro.xml", "classpath:spring/spring-redis.xml" })
public class OrganizationTest {

    @Autowired
    private OrganizationServiceImpl service;

    /**
     * 测试获取xml的状态节点
     */
    @Test
    public void XMLreturnValue() {
        HashMap<String, String> returnValue = ElementXMLUtils.returnValue("RUN_SUCCESSFULLY");
        System.out.println(returnValue.toString());
    }

    /**
     * 新增组织结构
     */
    @Test
    public void saveOrganization() {
        String id = UuidUtil.getUUID();
        Organization organization2 = new Organization();
        organization2.setId(id);
        organization2.setOrgName("测试111");
        organization2.setuId("1cb54fff435b4fff8aa7c1fa391f519b");
        organization2.setOrgkey(UuidUtil.getUUID());
        Integer i = service.saveOrganization(organization2, "0801ba63b13245339cc1bc6737054088");
        System.out.println(i + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * 查询所有的组织结构信息
     */
    @Test
    public void listOrganization() {
        long start = System.currentTimeMillis();
        List<Organization> list = service.listOrganizationBy(new HashMap<Object, Object>());
        long end = System.currentTimeMillis();
        for (Organization organization : list) {
            System.out.println(organization.toString());
        }
        System.out.println(end - start);
    }

    /**
     * 根据传入的map查询相应的组织结构
     */
    @Test
    public void listOrganizationBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "433971dcf96a4eb88fbb163f7ab56fce");
        // map.put("code", "01");
        // map.put("orgName", "总裁");
        // map.put("parentId", "0");
        map.put("createTime", "2018-03-23");
        // map.put("updateTime", "");
        // map.put("uId", "1cb54fff435b4fff8aa7c1fa391f519b");
        // map.put("his_permission", "");
        List<Organization> list = service.listOrganizationBy(map);
        for (Organization organization : list) {
            System.out.println(organization.toString());
        }
    }

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     */
    @Test
    public void updateOrganizationById() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "7d1ef3a8bc584d739ad3a30ea2ad6c82");
        // map.put("orgName", "dfsfsfsfsafasfsad");
        Integer i = service.updateOrganizationById(map);
        System.out.println(i);
    }

    /**
     * 伪删除（根据条件删除组织结构信息）(级联删除)
     */
    @Test
    public void deleteOrganizationByStatus() {
        String id = "5dd58617f174473888857f389c822c75";
        Integer i = service.deleteOrganizationByStatusCascade("aa", id);
        System.out.println(i);
    }

    /**
     * 根据id查询该节点下的所有子节点 ,构建成树
     */
    @Test
    public void TreeByOrgId() {
        JSONObject string = service.TreeByIdForSon("");
        System.out.println(string.toString());
    }

    /**
     * 查询该节点以及节点以下的
     */

    @Test
    public void orgshow() {
        try {
            List<String> ids = new ArrayList<String>();
            ids.add("f946a081eda949228537a8746200c3d6");
            ids.add("cced74c59a9846b5b0a81c0baf235c17");
            ids.add("f8483e1c85e84323853aeee27b4e8c91");
            ids.add("e71064dc0fc443fa8893ce489aed8c38");
            List<Organization> list = service.listOrganization(ids);
            List<String> listmap = new ArrayList<String>();
            for (Organization organization : list) {
                String his_permission = organization.getHis_permission();
                String[] hps = his_permission.split(",");// 分割逗号
                listmap.addAll(Arrays.asList(hps));// 所有的his_permission存到listmap当中
            }
            JSONObject obj = new JSONObject();
            // 查询对应的节点的数据
            List<Organization> listshow = service.listOrganizationcode(listmap);
            JSONArray json = JSONArray.fromObject(listshow);
            try {
                obj.put("rows", json);
                obj.put("resultCode", 200);
            } catch (Exception e) {
                obj.put("resultCode", 500);
                obj.put("resultDesc", "服务器异常");
            }
            System.out.println(json);
            for (Organization organization : listshow) {
                System.out.println(organization.toString());
            }
            System.out.println("展示" + listmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 根据id查询该节点下的所有父节点
     */
    @Test
    public void listTreeByOrgIdParent() {
        List<Organization> list = service.listTreeByIdForParent("135831766bf544e4a7f0f34f058116e7");
        for (Organization organization : list) {
            System.out.println(organization.toString());
        }
    }

    /**
     * 移动组织机构
     * @throws Exception 
     */
    @Test
    public void moveOrganization() {
        String id = "0801ba63b13245339cc1bc6737054088";
        String parentOrgId = "3020a10d0362455a9c6709562a908591";
        service.moveOrganization("dasdasdsadasdas", id, parentOrgId);
    }

    /**
     * 根据id或者name判断是否该节点存在子节点（这里的name主要是指公司名称，查询该公司是否有部门； 其他节点只能通过id查询）
     */
    @Test
    public void hasOrganizationSon() {
        Map<Object, Object> map = new HashMap<>();
        map.put("id", "3bc0148a12064532b4f0fef4ba08aa6d");// 组织id
        // map.put("orgName", "上市");// 组织架构名
        Boolean boolean1 = service.hasOrganizationSon(map);
        System.out.println(boolean1);
    }

    /**
     * 根据公司以下节点的id，查询到该节点所属公司
     */
    @Test
    public void getCompanyNameBySon() {
        Organization organization = service.getCompanyNameBySon("06e37893beb249b7a3a34eb4cc61d7ef");
        if (null != organization) {
            System.out.println(organization.toString());
        }
    }

    /**
     * 模拟根据json字符串生成html的table标签，方法一
     */
    @Test
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
    @Test
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
