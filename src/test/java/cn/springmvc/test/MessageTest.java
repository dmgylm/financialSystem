package cn.springmvc.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.financial.model.Message;
import cn.financial.service.impl.MessageServiceImpl;
import cn.financial.util.HttpClient3;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml",
        "classpath:spring/spring-cache.xml", "classpath:spring/spring-shiro.xml", "classpath:spring/spring-redis.xml" })
public class MessageTest {

    @Autowired
    private MessageServiceImpl service;
    
    private HttpClient3 http = new HttpClient3();
    
    /**
     * 根据用户权限展示相应的消息
     */
    @Test
    public void listMessageforPower() {
        http = new HttpClient3();
        try {
            String string = http.doPost(
                    "http://192.168.111.162:8083/financialSys/message/list?meta.session.id=99936cd4-4d75-4929-b607-70aa9f201690",
                    "");
            System.out.println(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // User user = new User();
        // user.setId("404ed3a5442c4ed78331d6c77077958f");
        // user.setName("aa");
        // JSONObject list = service.quartMessageByPower(user, 1, 10);
        // System.out.println(list.toString());
    }
    
    /**
     * 新增消息表
     */
    //@Test
    public void saveMessage() {
    	PropertyConfigurator.configure(MessageTest.class.getClassLoader().getResource("conf/log4j.properties"));

        Message message = new Message();
        message.setId(UuidUtil.getUUID());
        message.setStatus(0);
        message.setTheme(1);
        message.setContent("2018年维修表格需要填写");
        message.setuId("1d9fa5e93ffe46d78bb351ac05e70420");
        message.setsName("rr");
        Integer i = service.saveMessage(message);
        System.out.println(i);
    }

    /**
     * 根据消息状态展示消息列表
     */
    @Test
    public void listMessage() {
    	Map<Object,Object> map = new HashMap<Object,Object>();
    	map.put("uId", "1d9fa5e93ffe46d78bb351ac05e70420");
    	map.put("status","2");
		
		List<Message> list = service.listMessage(map);
		
		for (Message message : list) {
            System.out.println("status:" + message.getStatus());
            System.out.println("theme:" + message.getTheme());
            System.out.println("content:" + message.getContent());
            System.out.println("sName:" + message.getsName());
            System.out.println("createTime:" + message.getCreateTime());
            System.out.println("isTag:" + message.getIsTag());
            System.out.println("oId:"+message.getoId());
            System.out.println("uId:"+message.getuId());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
            /*int wstatus=0;
            for(int i=0;i<list.size();i++) {
            	if(list.get(i).getStatus()==0) {
            		wstatus+=1;
            	}
            }*/
            //System.out.println("未读消息条数"+wstatus);
            
    	}
    	/*map.put("uId","1d9fa5e93ffe46d78bb351ac05e70420" );
    	map.put("oId", "");*/
    	/*Calendar c = Calendar.getInstance();
    	int month = c.get(Calendar.MONTH)+1;
    	int year = c.get(Calendar.YEAR);
    	System.out.println("当前月份"+month);
    	System.out.println("当前年份"+year);*/
    	//map.put("isTag", 0);
    

    /**
     * 根据传入的map查询相应的消息表
     */
   
   /* public void listMessageBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        //map.put("id", "aa897350206245f4aeac083865d948c3");
        //map.put("status", 1);
        //map.put("theme", 1);
        map.put("content", "2018年金融表格需要填写");
        //map.put("createTime", "2018-03-16");
        //map.put("updateTime", "2018-03-16");
        map.put("uid", "1ccb0f2ca6224f389cbbea57b85d4458");
        //map.put("isTag", 0);
        List<Message> list = service.listMessageBy(map);
        for (Message message : list) {
            System.out.println("id:" + message.getId());
            System.out.println("status:" + message.getStatus());
            System.out.println("theme:" + message.getTheme());
            System.out.println("content:" + message.getContent());
            System.out.println("createTime:" + message.getCreateTime());
            System.out.println("updateTime:" + message.getUpdateTime());
            System.out.println("uid:" + message.getuId());
            System.out.println("isTag:" + message.getIsTag());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
*/
    /**
     * 根据ID查询消息表
     *//*
    public void getMessageById() {
        String id = "aa897350206245f4aeac083865d948c3";
        Message message = service.getMessageById(id);
        System.out.println("id:" + message.getId());
        System.out.println("status:" + message.getStatus());
        System.out.println("theme:" + message.getTheme());
        System.out.println("content:" + message.getContent());
        System.out.println("createTime:" + message.getCreateTime());
        System.out.println("updateTime:" + message.getUpdateTime());
        System.out.println("uid:" + message.getuId());
        System.out.println("isTag:" + message.getIsTag());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
*/
    /**
     * 根据条件修改消息表,这里是根据id来修改其他项,所以map中必须包含id
     */
    //@Test
    public void updateMessageById() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id","a900d71632d74ceab0979464213a005b");
        map.put("status", 1);
        map.put("isTag", 1);
        Integer i = service.updateMessageById(map);
        System.out.println(i);
    }

    /**
     * 删除（根据Id删除消息表）
     */
    /*public void deleteMessaageById() {
        String id = "ab55154719bc458f9985f84e05034196";
        Integer i = service.deleteMessageById(id);
        System.out.println(i);
    }*/
}
