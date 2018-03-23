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
      /*  String info="222";
        info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
        Capital capital=new Capital();
        capital.setId(UuidUtil.getUUID());
        capital.setoId("2222");
        capital.setInfo(info);
        capital.setCreateTime(sdf.parse("2018-02-18"));
        capital.setUpdateTime(sdf.parse("2018-03-20"));
        capital.setuId("6fcafcb22adf4c22a509184c96a828db");
        capital.setYear(2018);
        capital.setMonth(3);
        capital.setStatus(1);
        Integer i = capitalServiceImpl.insertCapital(capital);
        System.out.println(i);*/
    }

    /**
     * 查询所有的金额数据
     */
    @Test
    public void getAllCapital() {
       /* List<Capital> list = capitalServiceImpl.getAllCapital();
        System.out.println("所有的数据长度"+list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("第"+(i+1)+"条数据");
            System.out.println(list.get(i).getId()+"--"+list.get(i).getoId()+"--"
                     +list.get(i).getInfo()+"--"+sdf.format(list.get(i).getCreateTime())+"--"
                     +sdf.format(list.get(i).getUpdateTime())+"--"
                     +list.get(i).getuId()+"--"+list.get(i).getYear()+"--"
                     +list.get(i).getMonth()+"--"+list.get(i).getStatus());
            
        }*/
    }

    /**
     * 根据传入的map查询相应的金额表数据
     */
    @Test
    public void listCapitalBy() {
      /*  Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("oId", "111");
        List<Capital> list = capitalServiceImpl.listCapitalBy(map);
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("开始"+list.get(i).getId()+"--"+list.get(i).getoId()+"--"
                     +list.get(i).getInfo()+"--"+sdf.format(list.get(i).getCreateTime())+"--"
                     +sdf.format(list.get(i).getUpdateTime())+"--"
                     +list.get(i).getuId()+"--"+list.get(i).getYear()+"--"
                     +list.get(i).getMonth()+"--"+list.get(i).getStatus());
            
        }*/
    }

    /**
     * 根据ID查询资金信息
     */
    @Test
    public void getCapitalById(){
        /*String id = "b5d06e8791de436480d819835e32ab46";
        Capital capital = capitalServiceImpl.selectCapitalById(id);
        System.out.println("开始"+capital.getId()+"--"+capital.getoId()+"--"
                +capital.getInfo()+"--"+sdf.format(capital.getCreateTime())+"--"
                +sdf.format(capital.getUpdateTime())+"--"
                +capital.getuId()+"--"+capital.getYear()+"--"
                +capital.getMonth()+"--"+capital.getStatus());*/
    }

    /**
     * 根据条件修改资金信息
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void updateCapital() throws UnsupportedEncodingException, ParseException {
     /*   String info="111";
        info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
        Capital capital=new Capital();
        capital.setId("b5d06e8791de436480d819835e32ab46");
        capital.setoId("1");
        capital.setInfo(info);
        capital.setCreateTime(sdf.parse("2018-02-18"));
        capital.setUpdateTime(sdf.parse("2018-03-20"));
        capital.setuId("9685618f583c416ab835683d1eba09ea");
        capital.setYear(2018);
        capital.setMonth(1);
        capital.setStatus(1);
        Integer i = capitalServiceImpl.updateCapital(capital);
            System.out.println("结果"+i);*/
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
