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
import cn.financial.model.Statement;
import cn.financial.service.impl.StatementServiceImpl;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml", "classpath:conf/spring-cache.xml",
        "classpath:conf/spring-shiro.xml" })
public class StatementTest {

    @Autowired
    private StatementServiceImpl statementService;
    
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
        Statement statement=new Statement();
        statement.setId(UuidUtil.getUUID());
        statement.setoId("5");
        statement.setInfo(info);
        statement.setCreateTime(sdf.parse("2018-02-18"));
        statement.setUpdateTime(sdf.parse("2018-03-20"));
        statement.setTypeId("5");
        statement.setuId("9685618f583c416ab835683d1eba09ea");
        statement.setYear(2018);
        statement.setMonth(3);
        statement.setStatus(1);
        statement.setDelStatus(2);
        Integer i = statementService.insertStatement(statement);
        System.out.println(i);
    }

    /**
     * 查询所有的损益数据
     */
    @Test
    public void getAllStatement() {
        List<Statement> list = statementService.getAll();
        System.out.println("所有的数据长度"+list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("第"+(i+1)+"条数据");
            System.out.println(list.get(i).getId()+"--"+list.get(i).getoId()+"--"
                     +list.get(i).getInfo()+"--"+sdf.format(list.get(i).getCreateTime())+"--"
                     +sdf.format(list.get(i).getUpdateTime())+"--"+list.get(i).getTypeId()+"--"
                     +list.get(i).getuId()+"--"+list.get(i).getYear()+"--"
                     +list.get(i).getMonth()+"--"+list.get(i).getStatus());
            
        }
    }

    /**
     * 根据传入的map查询相应的损益表数据
     */
    @Test
    public void listStatementBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("id", "2");
        List<Statement> list = statementService.listStatementBy(map);
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("开始"+list.get(i).getId()+"--"+list.get(i).getoId()+"--"
                     +list.get(i).getInfo()+"--"+sdf.format(list.get(i).getCreateTime())+"--"
                     +sdf.format(list.get(i).getUpdateTime())+"--"+list.get(i).getTypeId()+"--"
                     +list.get(i).getuId()+"--"+list.get(i).getYear()+"--"
                     +list.get(i).getMonth()+"--"+list.get(i).getStatus());
            
        }
    }

    /**
     * 根据ID查询损益信息
     */
    @Test
    public void getStatementById(){
        String id = "2";
        Statement statement = statementService.selectStatementById(id);
        System.out.println("开始"+statement.getId()+"--"+statement.getoId()+"--"
                +statement.getInfo()+"--"+sdf.format(statement.getCreateTime())+"--"
                +sdf.format(statement.getUpdateTime())+"--"+statement.getTypeId()+"--"
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
        Statement statement=new Statement();
        statement.setId("2");
        statement.setoId("5");
        statement.setInfo(info);
        statement.setCreateTime(sdf.parse("2018-02-18"));
        statement.setUpdateTime(sdf.parse("2018-03-20"));
        statement.setTypeId("5");
        statement.setuId("9685618f583c416ab835683d1eba09ea");
        statement.setYear(2018);
        statement.setMonth(3);
        statement.setStatus(1);
        Integer i = statementService.updateStatement(statement);
            System.out.println("结果"+i);
     }

    /**
     * 伪删除（根据条件删除损益信息）
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void deleteStatement(){
        Integer i = statementService.deleteStatement("2");
         System.out.println("结果"+i);
    }

}
