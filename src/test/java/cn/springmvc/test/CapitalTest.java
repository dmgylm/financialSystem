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
import org.springframework.test.context.web.WebAppConfiguration;

import cn.financial.model.Capital;
import cn.financial.model.User;
import cn.financial.service.CapitalService;
import cn.financial.util.SiteConst;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml" ,"classpath:spring/spring-redis.xml"})
public class CapitalTest {

    @Autowired
    private CapitalService capitalService;
    
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
        capital.setoId("2a38648b34134859a018d080ae720573");
        capital.setCompany("盛世大联保险代理股份有限公司武汉分公司");
        capital.setAccountName("1");
        capital.setAccountBank("1");
        capital.setAccount("13445");
        capital.setAccountNature("123234");
        capital.setTradeTime(sdf.parse("2018-06-28 20:39:17"));
        capital.setStartBlack(1200.0);
        capital.setIncom(20000.0);
        capital.setPay(8000.1);
        capital.setEndBlack(1293.0);
        capital.setAbstrac("113");
        capital.setClassify("123");
        capital.setuId("1234");
        capital.setYear(2018);
        capital.setMonth(6);
        capital.setRemarks("23455");
        capital.setStatus(1);
        Integer i = capitalService.insertCapital(capital);
        System.out.println(i);
    }

    /**
     * 根据条件查资金数据 (不传数据就是查询所有的)
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void getAllCapital() throws UnsupportedEncodingException {
        Map<Object, Object> map = new HashMap<>();
        map.put("tradeTimeBeg","2018-06-01 00:00:00");
        map.put("tradeTimeEnd","2018-06-30 00:00:00");
        map.put("pageSize",10);
        map.put("start",1);
        List<Capital> list = capitalService.listCapitalBy(map);
        System.out.println("所有的数据长度"+list.size());
    }
    
    /**
     * 根据条件查资金数据 (不传数据就是查询所有的)  导出的查询方法
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void getCapital() throws UnsupportedEncodingException {
        Map<Object, Object> map = new HashMap<>();
        map.put("plate","1");
        List<Capital> list = capitalService.getAllCapital(map);
        System.out.println("所有的数据长度"+list.size());
    }
    
    
    /**
     * 根据ID查询资金信息
     */
    @Test
    public void getCapitalById(){
        String id = "b5d06e8791de436480d819835e32ab46";
        Capital capital = capitalService.selectCapitalById(id);
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
        Capital capital=new Capital();
        capital.setId("b5d06e8791de436480d819835e32ab46");
        capital.setCompany("1");
        capital.setAccountName("1");
        capital.setAccountBank("1");
        capital.setAccount("13445");
        capital.setAccountNature("123234");
        capital.setTradeTime(sdf.parse("2018-01-02"));
        capital.setStartBlack(1200.0);
        capital.setIncom(20000.0);
        capital.setPay(8000.0);
        capital.setEndBlack(1293.0);
        capital.setAbstrac("113");
        capital.setClassify("123");
        capital.setuId("1234");
        capital.setYear(2018);
        capital.setMonth(6);
        capital.setRemarks("23455");
        capital.setStatus(1);
        Integer i = capitalService.updateCapital(capital);
            System.out.println("结果"+i);
     }

    /**
     * 伪删除（根据条件删除资金信息）
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void deleteCapital(){
        String fileURL= SiteConst.CAPITALEXPORT;
        System.out.println("地址"+fileURL);
        /*Integer i = capitalService.deleteCapital("b5d06e8791de436480d819835e32ab46");
         System.out.println("结果"+i);*/
    }

}
