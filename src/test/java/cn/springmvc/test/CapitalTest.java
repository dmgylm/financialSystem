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
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml" ,"classpath:spring/spring-redis.xml"})
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
        String plate = "板块1";
        String BU = "事业部1";
        String regionName="大区名称1";
        String province="省份1";
        String city="城市1";
        String company="公司1";
        String accountName="账户名1";
        String accountBank="开户行1";
        String account="账户1";
        String accountNature="账户性质1";
        String tradeTime="2018-06-21";
        String startBlack="期初余额";
        String income="本期收入1";
        String pay="本期支出1";
        String endBlack="期末余额1";
        String abstrac="摘要1";
        String classify="项目分类1";
        String remarks="备注1";
        String json = "{\"plate\":\"" + plate + "\",\"BU\":" + BU+ ",\"regionName\":" + regionName+ ",\"province\":" +province+ ""
                + ",\"city\":" + city+ ",\"company\":" + company+ ",\"accountName\":" + accountName+ ",\"accountBank\":" + accountBank+ ""
                + ",\"account\":" + account+ ",\"accountNature\":" + accountNature+ ",\"tradeTime\":" + tradeTime+ ""
                + ",\"startBlack\":" + startBlack+ ",\"income\":" + income+ ",\"pay\":" + pay+ ",\"endBlack\":" + endBlack+ ""
                + ",\"abstrac\":" + abstrac+ ",\"classify\":" + classify+ ",\"remarks\":" + remarks+ "}";
        System.out.println(json);
        capital.setInfo(json);
        capital.setuId("1234");
        capital.setYear(2018);
        capital.setMonth(6);
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
        map.put("pageSize",10);
        map.put("start",1);
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
        capital.setId("46e59822b90445a9b0a13f3d7f053d5b");
        String plate = "板块3";
        String BU = "事业部3";
        String regionName="大区名称3";
        String province="省份3";
        String city="城市3";
        String company="公司3";
        String accountName="账户名3";
        String accountBank="开户行3";
        String account="账户3";
        String accountNature="账户性质3";
        String tradeTime="2018-06-21";
        String startBlack="期初余额3";
        String income="本期收入3";
        String pay="本期支出3";
        String endBlack="期末余额3";
        String abstrac="摘要3";
        String classify="项目分类3";
        String remarks="备注3";
        String json = "{\"plate\":\"" + plate + "\",\"BU\":" + BU+ ",\"regionName\":" + regionName+ ",\"province\":" +province+ ""
                + ",\"city\":" + city+ ",\"company\":" + company+ ",\"accountName\":" + accountName+ ",\"accountBank\":" + accountBank+ ""
                + ",\"account\":" + account+ ",\"accountNature\":" + accountNature+ ",\"tradeTime\":" + tradeTime+ ""
                + ",\"startBlack\":" + startBlack+ ",\"income\":" + income+ ",\"pay\":" + pay+ ",\"endBlack\":" + endBlack+ ""
                + ",\"abstrac\":" + abstrac+ ",\"classify\":" + classify+ ",\"remarks\":" + remarks+ "}";
        System.out.println(json);
        capital.setInfo(json);
        capital.setuId("9685618f583c416ab835683d1eba09ea");
        capital.setYear(2018);
        capital.setMonth(6);
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
