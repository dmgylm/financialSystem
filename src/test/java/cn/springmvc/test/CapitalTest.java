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
import cn.financial.model.Capital;
import cn.financial.model.User;
import cn.financial.service.impl.CapitalServiceImpl;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
public class CapitalTest {

    @Autowired
    private CapitalServiceImpl capitalServiceImpl;
    
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 新增资金数据
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void insertCapital() throws UnsupportedEncodingException, ParseException {
        Capital capital =new Capital();
        capital.setId(UuidUtil.getUUID());
        capital.setPlate("1235");
        capital.setBU("1234");
        capital.setRegionName("1111");
        capital.setProvince("1111");
        capital.setCity("11");
        capital.setCompany("1");
        capital.setAccountName("1");
        capital.setAccountBank("1");
        capital.setAccount("13445");
        capital.setAccountNature("123234");
        capital.setTradeTime("2018-01-02");
        capital.setStartBlack(1200);
        capital.setIncom(20000);
        capital.setPay(8000);
        capital.setEndBlack(1293);
        capital.setAbstrac("113");
        capital.setClassify("123");
        capital.setuId("1234");
        capital.setYear(2018);
        capital.setMonth(6);
        capital.setRemarks("23455");
        capital.setStatus(1);
        Integer i = capitalServiceImpl.insertCapital(capital);
        System.out.println(i);
    }

    /**
     * 根据条件查资金数据 (不传数据就是查询所有的)
     */
    @Test
    public void getAllCapital() {
        Map<Object, Object> map = new HashMap<>();
        List<Capital> list = capitalServiceImpl.listCapitalBy(map);
        System.out.println("所有的数据长度"+list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("第"+(i+1)+"条数据Id是："+list.get(i).getId());
        }
    }

    /**
     * 根据ID查询资金信息
     */
    @Test
    public void getCapitalById(){
        String id = "b5d06e8791de436480d819835e32ab46";
        Capital capital = capitalServiceImpl.selectCapitalById(id);
        System.out.println("开始"+capital.getId()+"--"+capital.getCreateTime()+"--"
                +capital.getUpdateTime()+"--"
                +capital.getuId()+"--"+capital.getYear()+"--"
                +capital.getMonth()+"--"+capital.getStatus());
    }

    /**
     * 根据条件修改资金信息
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void updateCapital() throws UnsupportedEncodingException, ParseException {
       /*String info="111";
        info = new String(info.getBytes("ISO-8859-1"), "UTF-8");*/
        Capital capital=new Capital();
        capital.setId("b5d06e8791de436480d819835e32ab46");
        capital.setuId("9685618f583c416ab835683d1eba09ea");
        capital.setYear(2018);
        capital.setMonth(1);
        capital.setStatus(1);
        Integer i = capitalServiceImpl.updateCapital(capital);
            System.out.println("结果"+i);
     }

    /**
     * 伪删除（根据条件删除资金信息）
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void deleteCapital(){
        Integer i = capitalServiceImpl.deleteCapital("b5d06e8791de436480d819835e32ab46");
         System.out.println("结果"+i);
    }

}
