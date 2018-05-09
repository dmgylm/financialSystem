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
import cn.financial.model.Budget;
import cn.financial.service.impl.BudgetServiceImpl;
import cn.financial.util.UuidUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml", "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-mybatis.xml", "classpath:conf/mybatis-config.xml" })
public class budgetTest {

    @Autowired
    private BudgetServiceImpl budgetServiceImpl;
    
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 新增预算数据
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void insertBudget() throws UnsupportedEncodingException, ParseException {
        String info="33333";
        info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
        Budget budget=new Budget();
        budget.setId(UuidUtil.getUUID());
        budget.setoId("3333");
        budget.setInfo(info);
        budget.setCreateTime("2018-02-18");
        budget.setUpdateTime("2018-03-20");
        budget.setTypeId("333");
        budget.setuId("6fcafcb22adf4c22a509184c96a828db");
        budget.setYear(2018);
        budget.setMonth(3);
        budget.setStatus(0);
        Integer i = budgetServiceImpl.insertBudget(budget);
        System.out.println(i);
    }

    /**
     * 查询所有的预算数据
     */
    @Test
    public void getAllBudget() {
        List<Budget> list =budgetServiceImpl.getAllBudget(); 
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
     * 根据传入的map查询相应的预算表数据
     */
    @Test
    public void listBudgetBy() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("oId", "111");
        List<Budget> list = budgetServiceImpl.listBudgetBy(map);
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
     * 根据ID查询资金信息
     */
    @Test
    public void getBudgetById(){
        String id = "792a654bcad84f15bc808df63a805581";
        Budget budget =budgetServiceImpl.selectBudgetById(id);
        System.out.println("开始"+budget.getId()+"--"+budget.getoId()+"--"
                +budget.getInfo()+"--"+sdf.format(budget.getCreateTime())+"--"
                +sdf.format(budget.getUpdateTime())+"--"+budget.getTypeId()+"--"
                +budget.getuId()+"--"+budget.getYear()+"--"
                +budget.getMonth()+"--"+budget.getStatus());
    }

    /**
     * 根据条件修改资金信息
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void updateBudget() throws UnsupportedEncodingException, ParseException {
        String info="1";
        info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
        Budget budget=new Budget();
        budget.setId("792a654bcad84f15bc808df63a805581");
        budget.setoId("1");
        budget.setInfo(info);
        budget.setCreateTime("2018-02-18");
        budget.setUpdateTime("2018-03-20");
        budget.setTypeId("1");
        budget.setuId("9685618f583c416ab835683d1eba09ea");
        budget.setYear(2018);
        budget.setMonth(1);
        budget.setStatus(1);
        Integer i = budgetServiceImpl.updateBudget(budget);
            System.out.println("结果"+i);
     }

    /**
     * 伪删除（根据条件删除资金信息）
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
    @Test
    public void deleteBudget(){
        Integer i = budgetServiceImpl.deleteBudget("9e55a7d1a2a6456e8b44ac2aec95988c");
         System.out.println("结果"+i);
    }

}
