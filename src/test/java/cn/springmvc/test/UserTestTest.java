package cn.springmvc.test;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.service.OrganizationService;
import cn.financial.service.impl.OrganizationServiceImpl;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
public class UserTestTest {

    @Autowired
    private OrganizationServiceImpl service;

    /**
     * 新增组织结构
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void saveOrganization() throws UnsupportedEncodingException {
        String str = "任意字符串";
        try {
            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(str);
        String id = UuidUtil.getUUID();
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", id);
        map.put("orgName", new String(str.getBytes("ISO-8859-1"), "UTF-8"));
        map.put("uId", "1");
        map.put("parentId", "0");
        map.put("createTime", new Date());
        map.put("updateTime", new Date());
        int i = service.saveOrganization(map);
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
        map.put("uId", "2");
        List<Organization> list = service.listOrganizationBy(map);
        for (Organization organization : list) {
            System.out.println(organization.getId() + "---------------------------------------------"
                    + organization.getOrgName() + "---------------------------------------------"
                    + organization.getParentId() + "---------------------------------------------"
                    + organization.getCreateTime() + "---------------------------------------------"
                    + organization.getUpdateTime());
        }
    }

    /**
     * 根据ID查询组织结构信息
     */
    @Test
    public void getOrganization() {
        String id = "7963dbc544024729927c41ce8238db40";
        Organization organization = service.getOrganization(id);
        System.out
                .println(organization.getId()
                        + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
                        + organization.getOrgName() + "---------------------------------------------"
                        + organization.getParentId() + "---------------------------------------------"
                        + organization.getCreateTime() + "---------------------------------------------"
                        + organization.getUpdateTime());
    }

    /**
     * 根据条件修改组织结构信息
     */
    @Test
    public void updateOrganization() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "7963dbc544024729927c41ce8238db40");
        map.put("orgName", "bb");
        int i = service.updateOrganization(map);
        System.out.println(i);
    }

    /**
     * 根据条件删除组织结构信息
     */
    @Test
    public void deleteOrganization() {
        String id = "1";
        int i = service.deleteOrganization(id);
        System.out.println(1);
    }

}
