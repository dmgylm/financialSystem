package cn.financial.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.financial.dao.CapitalDao;
import cn.financial.model.Capital;
import cn.financial.service.CapitalService;

/**
 * 资金表ServiceImpl
 * @author lmn
 *
 */
@Service("CapitalServiceImpl")
public class CapitalServiceImpl implements CapitalService{
    @Autowired
    private CapitalDao capitalDao;

    /**
     * 新增资金表数据
     */
    @Override
    public Integer insertCapital(Capital capital) {
        // TODO Auto-generated method stub
        return capitalDao.insertCapital(capital);
    }
    
    @Override
    public Integer batchInsertCapital(List<Capital> listCapital) {
        // TODO Auto-generated method stub
        Integer a=0;
        for (int i = 0; i < listCapital.size(); i++) {
             a=capitalDao.insertCapital(listCapital.get(i));
        }
        return a;
    }

    /**
     * 修改资金表数据
     */
    @Override
    public Integer updateCapital(Capital capital) {
        // TODO Auto-generated method stub
        return capitalDao.updateCapital(capital);
    }

  /*  *//**
     * 查询所有的资金表数据
     *//*
    @Override
    public List<Capital> getAllCapital() {
        // TODO Auto-generated method stub
        return capitalDao.getAllCapital();
    }*/

    /**
     * 根据id查询资金表数据
     */
    @Override
    public Capital selectCapitalById(String id) {
        // TODO Auto-generated method stub
        String endId="";
        if(id!=null&&!id.equals("")){
            endId=id;
        }
        return capitalDao.selectCapitalById(endId);
    }

    /**
     * 根据条件资金表数据
     */
    @Override
    public List<Capital> listCapitalBy(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        Map<Object, Object> resultMap=new HashMap<>();
        if(map.get("plate").toString()!=null&&!map.get("plate").equals("")){
            resultMap.put("plate", map.get("plate").toString());
        }
        if(map.get("BU").toString()!=null&&!map.get("BU").equals("")){
            resultMap.put("BU", map.get("BU").toString());
        }
        if(map.get("regionName").toString()!=null&&!map.get("regionName").equals("")){
            resultMap.put("regionName", map.get("regionName").toString());
        }
        if(map.get("province").toString()!=null&&!map.get("province").equals("")){
            resultMap.put("province", map.get("province").toString());
        }
        if(map.get("company").toString()!=null&&!map.get("company").equals("")){
            resultMap.put("company", map.get("company").toString());
        }
        if(map.get("accountBank").toString()!=null&&!map.get("accountBank").equals("")){
            resultMap.put("accountBank", map.get("accountBank").toString());
        }
        if(map.get("tradeTimeBeg").toString()!=null&&!map.get("tradeTimeBeg").equals("")){
            resultMap.put("tradeTimeBeg", map.get("tradeTimeBeg").toString());
        }
        if(map.get("tradeTimeEnd").toString()!=null&&!map.get("tradeTimeEnd").equals("")){
            resultMap.put("tradeTimeEnd", map.get("tradeTimeEnd").toString());
        }
        if(map.get("classify").toString()!=null&&!map.get("classify").equals("")){
            resultMap.put("classify", map.get("classify").toString());
        }
        Integer pageSize=10;
        if(map.get("pageSize").toString()!=null && !map.get("pageSize").toString().equals("")){
            pageSize=Integer.parseInt(map.get("pageSize").toString());
            map.put("pageSize",pageSize);
        }else{
            map.put("pageSize",pageSize);
        }
        Integer start=0;
        if(map.get("page").toString()!=null && !map.get("page").toString().equals("")){
            start=pageSize * (Integer.parseInt(map.get("page").toString()) - 1);
            map.put("start",start);
        }else{
            map.put("start",start);
        }
        resultMap.put("oId", map.get("oId").toString());
        return capitalDao.listCapitalBy(resultMap);
    }

    /**
     * 修改资金表数据 
     */
    @Override
    public Integer deleteCapital(String id) {
        // TODO Auto-generated method stub
        return capitalDao.deleteCapital(id);
    }

    
    /**
     * 查询所有的查询数据
     */
    @Override
    public List<Capital> getAllCapital(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        return capitalDao.getAllCapital(map);
    }

    @Override
    public List<Capital> capitalExport(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        Map<Object, Object> resultMap=new HashMap<>();
        if(map.get("plate").toString()!=null&&!map.get("plate").equals("")){
            resultMap.put("plate", map.get("plate").toString());
        }
        if(map.get("BU").toString()!=null&&!map.get("BU").equals("")){
            resultMap.put("BU", map.get("BU").toString());
        }
        if(map.get("regionName").toString()!=null&&!map.get("regionName").equals("")){
            resultMap.put("regionName", map.get("regionName").toString());
        }
        if(map.get("province").toString()!=null&&!map.get("province").equals("")){
            resultMap.put("province", map.get("province").toString());
        }
        if(map.get("company").toString()!=null&&!map.get("company").equals("")){
            resultMap.put("company", map.get("company").toString());
        }
        if(map.get("accountBank").toString()!=null&&!map.get("accountBank").equals("")){
            resultMap.put("accountBank", map.get("accountBank").toString());
        }
        if(map.get("tradeTimeBeg").toString()!=null&&!map.get("tradeTimeBeg").equals("")){
            resultMap.put("tradeTimeBeg", map.get("tradeTimeBeg").toString());
        }
        if(map.get("tradeTimeEnd").toString()!=null&&!map.get("tradeTimeEnd").equals("")){
            resultMap.put("tradeTimeEnd", map.get("tradeTimeEnd").toString());
        }
        if(map.get("classify").toString()!=null&&!map.get("classify").equals("")){
            resultMap.put("classify", map.get("classify").toString());
        }
        resultMap.put("oId", map.get("oId").toString());
        return capitalDao.capitalExport(resultMap);
    }

}
 