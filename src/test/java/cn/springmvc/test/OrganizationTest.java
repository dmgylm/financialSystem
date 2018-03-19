package cn.springmvc.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Organization;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
public class OrganizationTest {

    @Autowired
    private OrganizationServiceImpl service;

    /**
     * 新增组织结构
     */
    @Test
    public void saveOrganization() {
        String id = UuidUtil.getUUID();
        Organization organization = new Organization();
        organization.setId(id);
        organization.setOrgName("测试222");
        organization.setParentId("01");
        organization.setuId("1cb54fff435b4fff8aa7c1fa391f519b");
        Integer i = service.saveOrganization(organization,"22be27739af342e7b10b54d5af1de6f1");
        System.out.println(i);
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

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     */
    @Test
    public void updateOrganizationById() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "22be27739af342e7b10b54d5af1de6f1");
        map.put("parentId", "1");
        Integer i = service.updateOrganizationById(map);
        System.out.println(i);
    }

    /**
     * 伪删除（根据条件删除组织结构信息）
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
    public void listTreeByOrgId() {
        String id = "dc435e2de0fa4fc68b650faeddfaccf9";
        String string = service.listTreeByOrgId(id);
        System.out.println(string);
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

}
