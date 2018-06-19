package cn.financial.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.financial.dao.MongoDBDao;
import cn.financial.model.Orderate;
import cn.financial.model.DataTest;
import cn.financial.model.NetWork;

@Service("MongoDBServiceImpl")
public class MongoDBServiceImpl {

	@Autowired
	private MongoDBDao mongodbDao; 
	
	
	public List<Orderate> findList(){
		return mongodbDao.findAll();
	}
	
	public Orderate findById(String id){
		return mongodbDao.findById(id);
	}
	
	public void insertOrderate(Orderate orderate){
		mongodbDao.insert(orderate);
	}
	
	public void insertTests(NetWork netWork){
		mongodbDao.insert(netWork);
	}
	public void insertTests1(NetWork netWork){
		Calendar c = Calendar.getInstance();
		long s = c.getTimeInMillis();
		System.out.println("start:"+s);
		List<NetWork> ss=mongodbDao.findAll1();
//		List<NetWork> ss=mongodbDao.findAll1s();
		
		c = Calendar.getInstance();
		long e = c.getTimeInMillis();
		System.out.println("end:"+e);
		System.out.println(s-e);
		System.out.println(ss.size());
		
	}
	
	public void insertData(DataTest dataTest){
		mongodbDao.insert(dataTest);
	}
	
	public void findData(){
		Calendar c = Calendar.getInstance();
		long s = c.getTimeInMillis();
		System.out.println("start:"+s);
		List<DataTest> ss=mongodbDao.findAll3();
		
		c = Calendar.getInstance();
		long e = c.getTimeInMillis();
		System.out.println("end:"+e);
		System.out.println(s-e);
		System.out.println(ss.size());
		
	}
}
