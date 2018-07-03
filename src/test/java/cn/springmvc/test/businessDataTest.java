package cn.springmvc.test;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cn.financial.model.BusinessData;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml" ,"classpath:spring/spring-redis.xml"})
public class businessDataTest {

    @Autowired
    private BusinessDataServiceImpl businessDataService;
    
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 新增损益数据
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void insertStatement() throws UnsupportedEncodingException, ParseException {
        String info="第wu个";
        info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
        BusinessData businessData=new BusinessData();
        businessData.setId(UuidUtil.getUUID());
        businessData.setoId("5");
        businessData.setInfo(info);
        businessData.setTypeId("5");
        businessData.setuId("9685618f583c416ab835683d1eba09ea");
        businessData.setYear(2018);
        businessData.setMonth(3);
        businessData.setStatus(1);
        businessData.setDelStatus(2);
        businessData.setsId(1);
        Integer i = businessDataService.insertBusinessData(businessData);
        System.out.println(i);
    }

    /**
     * 根据传入的map查询相应的损益表数据 不传就是查询 全部
     */
    @Test
    public void listStatementBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("year",2018);
        map.put("month",4);
        map.put("sId", 1);
        List<BusinessData> list = businessDataService.listBusinessDataBy(map);
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("开始"+list.get(i).getId()+"--"+list.get(i).getoId()+"--"
                     +list.get(i).getInfo()+"--"+"--"+list.get(i).getTypeId()+"--"
                     +list.get(i).getuId()+"--"+list.get(i).getYear()+"--"
                     +list.get(i).getMonth()+"--"+list.get(i).getStatus());
            
        }
    }

    /**
     * 根据ID查询损益信息
     */
    @Test
    public void getStatementById(){
        String id = "ca85a6e99b8949a886ee50efac06ab06";
        BusinessData statement = businessDataService.selectBusinessDataById(id);
        System.out.println("开始"+statement.getId()+"--"+statement.getoId()+"--"
                +statement.getInfo()+"--"+"--"+statement.getTypeId()+"--"
                +statement.getuId()+"--"+statement.getYear()+"--"
                +statement.getMonth()+"--"+statement.getStatus());
    }

    /**
     * 根据条件修改损益信息
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void updateStatement() throws UnsupportedEncodingException, ParseException {
        String info="第2个";
        info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
        BusinessData statement=new BusinessData();
        statement.setId("2");
        statement.setoId("5");
        statement.setInfo(info);
        statement.setTypeId("5");
        statement.setuId("9685618f583c416ab835683d1eba09ea");
        statement.setYear(2018);
        statement.setMonth(3);
        statement.setStatus(1);
        statement.setsId(1);
        Integer i = businessDataService.updateBusinessData(statement);
            System.out.println("结果"+i);
     }

    /**
     * 伪删除（根据条件删除损益信息）
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void deleteStatement(){
        Integer i = businessDataService.deleteBusinessData("2");
         System.out.println("结果"+i);
    }

}
