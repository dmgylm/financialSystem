package cn.springmvc.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Organization;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.JsonToHtmlUtil;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml" })
public class OrganizationTest {

    @Autowired
    private OrganizationServiceImpl service;

    /**
     * 新增组织结构
     */
    @Test
    public void saveOrganization() {
        String id = UuidUtil.getUUID();
        Organization organization2 = new Organization();
        organization2.setId(id);
        organization2.setOrgName("2312312");
        organization2.setuId("1cb54fff435b4fff8aa7c1fa391f519b");
        Integer i = service.saveOrganization(organization2, "");
        System.out.println(i + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * 查询所有的组织结构信息
     */
    @Test
    public void listOrganization() {
        List<Organization> list = service.listOrganization();
        for (Organization organization : list) {
            System.out.println("id:" + organization.getId());
            System.out.println("code:" + organization.getCode());
            System.out.println("orgName:" + organization.getOrgName());
            System.out.println("parentId:" + organization.getParentId());
            System.out.println("createTime:" + organization.getCreateTime());
            System.out.println("updateTime:" + organization.getUpdateTime());
            System.out.println("uId:" + organization.getuId());
            System.out.println("his_permission:" + organization.getHis_permission());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    /**
     * 根据传入的map查询相应的组织结构
     */
    @Test
    public void listOrganizationBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        // map.put("id", "22be27739af342e7b10b54d5af1de6f1");
        map.put("code", "01");
        // map.put("orgName", "总裁");
        // map.put("parentId", "0");
        // map.put("createTime", "2018-03-16");
        // map.put("updateTime", "");
        map.put("uId", "1cb54fff435b4fff8aa7c1fa391f519b");
        // map.put("his_permission", "");
        List<Organization> list = service.listOrganizationBy(map);
        for (Organization organization : list) {
            System.out.println("id:" + organization.getId());
            System.out.println("code:" + organization.getCode());
            System.out.println("orgName:" + organization.getOrgName());
            System.out.println("parentId:" + organization.getParentId());
            System.out.println("createTime:" + organization.getCreateTime());
            System.out.println("updateTime:" + organization.getUpdateTime());
            System.out.println("uId:" + organization.getuId());
            System.out.println("his_permission:" + organization.getHis_permission());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    /**
     * 根据ID查询组织结构信息
     */
    @Test
    public void getOrganizationById() {
        String id = "22be27739af342e7b10b54d5af1de6f1";
        Organization organization = service.getOrganizationById(id);
        if (organization != null) {
            System.out.println("id:" + organization.getId());
            System.out.println("code:" + organization.getCode());
            System.out.println("orgName:" + organization.getOrgName());
            System.out.println("parentId:" + organization.getParentId());
            System.out.println("createTime:" + organization.getCreateTime());
            System.out.println("updateTime:" + organization.getUpdateTime());
            System.out.println("uId:" + organization.getuId());
            System.out.println("his_permission:" + organization.getHis_permission());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     */
    @Test
    public void updateOrganizationById() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "fca475be608d478a8b33b83e49546f77");
        map.put("orgName", "融资11");
        Integer i = service.updateOrganizationById(map);
        System.out.println(i);
    }

    /**
     * 伪删除（根据条件删除组织结构信息）(级联删除)
     */
    @Test
    public void deleteOrganizationByStatus() {
        String id = "be5c0addafc04dd5b7a7455d95466138";
        Integer i = service.deleteOrganizationByStatusCascade(id);
        System.out.println(i);
    }

    /**
     * 根据id查询该节点下的所有子节点 ,构建成树
     */
    @Test
    public void TreeByOrgId() {
        String string = service.TreeByIdForSon("cced74c59a9846b5b0a81c0baf235c17");
        System.out.println(string);
    }

    /**
     * 根据id查询该节点下的所有子节点 的集合
     */
    @Test
    public void listTreeByOrgId() {
        List<Organization> list = service.listTreeByIdForSon("cced74c59a9846b5b0a81c0baf235c17");
        for (Organization organization : list) {
            System.out.println("id:" + organization.getId());
            System.out.println("code:" + organization.getCode());
            System.out.println("orgName:" + organization.getOrgName());
            System.out.println("parentId:" + organization.getParentId());
            System.out.println("createTime:" + organization.getCreateTime());
            System.out.println("updateTime:" + organization.getUpdateTime());
            System.out.println("uId:" + organization.getuId());
            System.out.println("his_permission:" + organization.getHis_permission());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    /**
     * 根据id查询该节点下的所有父节点
     */
    @Test
    public void listTreeByOrgIdParent() {
        List<Organization> list = service.listTreeByIdForParent("fd99bcb243b0473f865a243538d3d080");
        for (Organization organization : list) {
            System.out.println("id:" + organization.getId());
            System.out.println("code:" + organization.getCode());
            System.out.println("orgName:" + organization.getOrgName());
            System.out.println("parentId:" + organization.getParentId());
            System.out.println("createTime:" + organization.getCreateTime());
            System.out.println("updateTime:" + organization.getUpdateTime());
            System.out.println("uId:" + organization.getuId());
            System.out.println("his_permission:" + organization.getHis_permission());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    /**
     * 移动组织机构
     */
    @Test
    public void moveOrganization() {
        String id = "ed883ea05ec344e9a898727a16f01859";
        String parentOrgId = "ad2d83626a1846b68186775a3c1e8068";
        service.moveOrganization(id, parentOrgId);
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
        Organization organization = service.getCompanyNameBySon("1a7458a2d2f44c17873c2c82aee320dd");
        if (null != organization) {
            System.out.println("id:" + organization.getId());
            System.out.println("code:" + organization.getCode());
            System.out.println("orgName:" + organization.getOrgName());
            System.out.println("parentId:" + organization.getParentId());
            System.out.println("createTime:" + organization.getCreateTime());
            System.out.println("updateTime:" + organization.getUpdateTime());
            System.out.println("uId:" + organization.getuId());
            System.out.println("his_permission:" + organization.getHis_permission());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    // /**
    // * 模拟根据json字符串生成html的table标签，方法一
    // */
    // @Test
    // public void toHtml() {
    // String jsonOfStr = "{\"biaoming\":null,\"gongsiming\":null,"
    // + "\"yewu\":null,\"bumen\":null,\"year\":null,\"month\":null,"
    // + "\"data\":{\"bxyye\":{\"heji\":{\"benyueshiji\":null,"
    // + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
    // + "\"wanchenglv\":null},\"jiaoqiangxian\":{\"benyueshiji\":null,"
    // + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
    // + "\"wanchenglv\":null},\"shangyexian\":{\"benyueshiji\":null,"
    // + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
    // + "\"wanchenglv\":null}},\"bxyw\":{\"shouru\":{\"benyueshiji\":null,"
    // + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
    // + "\"wanchenglv\":null},\"chengben\":{\"benyueshiji\":null,"
    // + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
    // + "\"wanchenglv\":null},\"rengongchengben\":{\"benyueshiji\":null,"
    // + "\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,"
    // + "\"wanchenglv\":null}}}}";
    //
    // String jj =
    // "{\"biaoming\":null,\"gongsiming\":null,\"yewu\":null,\"bumen\":null,\"year\":null,\"month\":null,\"data\":{\"aaaaaa\":{\"bxyye\":{\"heji\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"jiaoqiangxian\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"shangyexian\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}},\"bxyw\":{\"shouru\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"chengben\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null},\"rengongchengben\":{\"benyueshiji\":null,\"benyuebudian\":null,\"budianhoushiji\":null,\"benyueyusuan\":null,\"wanchenglv\":null}}}}}";
    //
    // JSONObject jsonObject1 = JSONObject.fromObject(jj);
    // JSONObject data =
    // JSONObject.fromObject(jsonObject1.get("data").toString());
    // String result = "<table border=\"1\">";
    // List<String> listtd = new ArrayList<>();
    // List<String> listtr = new ArrayList<>();
    // List<JSONObject> listtr1 = new ArrayList<>();
    // String rStr = JsonToHtmlUtil.jsonToTable(data, listtd, listtr, listtr1);
    // String dStr = "<tr>";
    // for (String string : listtd) {
    // dStr += ("<td>" + string + "</td>");
    // }
    // dStr += "</tr>";
    // result = result + dStr + rStr + "</table>";
    // for (int i = 0; i < listtr1.size(); i++) {
    // List<String> listend = new ArrayList<>();
    // JsonToHtmlUtil.getNum(listtr1.get(i), listend);
    // int rowspan = listend.size() + 1;
    // if (rowspan > 1) {
    // result = result.replace("<td>" + listtr.get(i).toString() + "</td>",
    // "<td rowspan=\"" + rowspan + "\">"
    // + listtr.get(i).toString() + "</td>");
    // } else {
    // String td = "";
    // for (int j = 0; j < listtd.size(); j++) {
    // td += "<td></td>";
    // }
    // result = result.replace("<td>" + listtr.get(i).toString() + "</td>",
    // "<td rowspan=\"" + rowspan + "\">"
    // + listtr.get(i).toString() + "</td>" + td);
    // }
    // }
    // System.out.println(result);
    // }

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

        JSONObject jsonObject = JSONObject.fromObject(jj);
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
                result = result.replace("<td>" + listtr.get(i).toString() + "</td>", "<td rowspan=\"" + rowspan + "\">"
                        + listtr.get(i).toString() + "</td>");
            } else {
                result = result.replace("<td>" + listtr.get(i).toString() + "</td>", "<td rowspan=\"" + rowspan + "\">"
                        + listtr.get(i).toString() + "</td>");
            }
        }

        System.out.println(result);
    }
}
