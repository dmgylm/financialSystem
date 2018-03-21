package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Resource;
import cn.financial.service.impl.ResourceServiceImpl;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml"})
/**
 * 资源权限表测试
 * @author gs
 * 2018/3/15
 */
public class ResourceTest {
    @Autowired
    private ResourceServiceImpl service;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        Resource resource = new Resource();
        resource.setId(UuidUtil.getUUID());
        resource.setName("录入中心");
        resource.setUrl("");
        resource.setParentId("44444444");
        resource.setPermssion("333");
        resource.setCreateTime(new Date());
        try {
            System.out.println(service.insertResource(resource));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //修改
    @Test
    public void updateTest() {
        Resource resource = new Resource();
        resource.setId("1111");
        resource.setName("汇总中心");
        resource.setUrl("/url/ttttt");
        resource.setParentId("6574323111111111");
        resource.setPermssion("76866432");
        resource.setUpdateTime(new Date());
        try {
            System.out.println(service.updateResource(resource));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //删除
    @Test
    public void deleteTest() {   
        System.out.println(service.deleteResource("2222"));
    }
    //查询全部
    @Test
    public void ListRoleTest() {
        List<Resource> resource = service.listResource();
        for(Resource list:resource){
            String uTime = "";
            if(list.getUpdateTime()!=null && !"".equals(list.getUpdateTime())){
                uTime = formatter.format(list.getUpdateTime());
            }
            System.out.println("id: "+list.getId() +" name: "+list.getName() +" code: "+list.getCode()+
                    " url: "+list.getUrl()+" parentId: "+list.getParentId()+" permssion: "+list.getPermssion()+
                    " createTime: "+formatter.format(list.getCreateTime()) +" updateTime: "+uTime +"\n"); 
        }
        
    }
    //根据id/name查询
    @Test
    public void ListByIdTest() {
        //7d47d7e96d304313834b0c0d4953abe9  资源
        Resource resource = service.getResourceById("35a51fa5b6ed49ad920b30055ea9f4c6","");
        String uTime = "";
        if(resource.getUpdateTime()!=null && !"".equals(resource.getUpdateTime())){
            uTime = formatter.format(resource.getUpdateTime());
          }
        System.out.println("id: "+resource.getId()+" name: "+resource.getName()+" code: "+resource.getCode()+
                " url: "+resource.getUrl()+" parentId: "+resource.getParentId()+" permssion: "+resource.getPermssion()+
                " createTime: "+formatter.format(resource.getCreateTime())+" updateTime: "+uTime);
    }
}
