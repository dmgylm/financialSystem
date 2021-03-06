package cn.financial.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public Integer insertCapital(Capital capital) {
        // TODO Auto-generated method stub
        return capitalDao.insertCapital(capital);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
        return capitalDao.listCapitalBy(map);
    }

    /**
     * 删除资金表数据 
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
       
        return capitalDao.capitalExport(map);
    }
    
    
    /**
     * 文件导出
     */
    public Boolean export(HttpServletRequest request, HttpServletResponse response,File file) throws IOException {
        Boolean result=true;
            if(file.exists()) {
                BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
                byte[] buf = new byte[1024];
                int len = 0;
                response.reset();
                response.setContentType("application/x-msdownload"); 
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                OutputStream out = response.getOutputStream();
                while((len = br.read(buf)) > 0) {
                    out.write(buf,0,len);
                }
                br.close();
                out.close();
            }else{
            result=false;
        }
        return result;
    }

    @Override
    public List<Capital> capitalCompany(Map<Object, Object> map) {
        // TODO Auto-generated method stub
        return capitalDao.capitalCompany(map);
    }

  
}
 