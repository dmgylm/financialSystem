package cn.springmvc.test;

import java.io.UnsupportedEncodingException;
import java.util.Date;
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
        String orgName = "任意字符串";
        try {
            orgName = new String(orgName.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(orgName);
        String id = UuidUtil.getUUID();
        Organization organization = new Organization();
        organization.setId(id);
        organization.setOrgName(orgName);
        organization.setuId("we12wqsas3121312sasq21qw");
        organization.setParentId("wsdvf2jkil6yytgfgfd2");
        organization.setCreateTime(new Date());
        Integer i = service.saveOrganization(organization);
        System.out.println(i);
    }

    /**
     * 查询所有的组织结构信息
     */
    @Test
    public void listOrganization() {
        List<Organization> list = service.listOrganization();
        for (Organization organization : list) {
            System.out.println(organization.getId() + "---------------------------------------------"
                    + organization.getOrgName() + "---------------------------------------------"
                    + organization.getParentId() + "---------------------------------------------"
                    + organization.getCreateTime() + "---------------------------------------------"
                    + organization.getUpdateTime() + "---------------------------------------------"
                    + organization.getuId());
        }
    }

    /**
     * 根据传入的map查询相应的组织结构
     */
    @Test
    public void listOrganizationBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "35d883977eea4ddb94c1482349b61461");
        List<Organization> list = service.listOrganizationBy(map);
        for (Organization organization : list) {
            System.out.println(organization.getId() + "---------------------------------------------"
                    + organization.getOrgName() + "---------------------------------------------"
                    + organization.getParentId() + "---------------------------------------------"
                    + organization.getCreateTime() + "---------------------------------------------"
                    + organization.getUpdateTime() + "---------------------------------------------"
                    + organization.getuId());
        }
    }

    /**
     * 根据ID查询组织结构信息
     */
    @Test
    public void getOrganizationById() {
        String id = "35d883977eea4ddb94c1482349b61461";
        Organization organization = service.getOrganizationById(id);
        System.out
                .println(organization.getId()
                        + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                        + organization.getOrgName() + "---------------------------------------------"
                        + organization.getParentId() + "---------------------------------------------"
                        + organization.getCreateTime() + "---------------------------------------------"
                        + organization.getUpdateTime() + "---------------------------------------------"
                        + organization.getuId());
    }

    /**
     * 根据条件修改组织结构信息,这里是根据id来修改其他项,所以map中必须包含id
     */
    @Test
    public void updateOrganizationById() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "35d883977eea4ddb94c1482349b61461");
        map.put("uId", "1ccb0f2ca6224f389cbbea57b85d4458");
        map.put("updateTime", new Date());
        Integer i = service.updateOrganizationById(map);
        System.out.println(i);
    }

    /**
     * 伪删除（根据条件删除组织结构信息）
     */
    @Test
    public void deleteOrganizationByStatus() {
        String id = "35d883977eea4ddb94c1482349b61461";
        Integer i = service.deleteOrganizationByStatus(id);
        System.out.println(i);
    }

}
