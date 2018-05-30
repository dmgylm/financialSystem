package cn.financial.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.financial.model.Orderate;
import cn.financial.model.DataTest;
import cn.financial.model.NetWork;

@Repository
public class MongoDBDao {

	@Autowired  
    private MongoTemplate mongoTemplate;
	
	
	 /**  
     * 新增  
     * <br>------------------------------<br>  
     * @param orderate  
     */   
    public void insert(Orderate orderate) {  
        mongoTemplate.insert(orderate);  
    }  
    public void insert(DataTest dataTest) {  
        mongoTemplate.insert(dataTest);  
    }  
    
    public void insert(NetWork netWork){
    	mongoTemplate.insert(netWork);  
    }
    
    /**  
     * 批量新增  
     * <br>------------------------------<br>  
     * @param orderates  
     */  
    public void insertAll(List<Orderate> orderates) {  
        mongoTemplate.insertAll(orderates); 
    }  
	
    /**  
     * 删除,按主键id, 如果主键的值为null,删除会失败  
     * <br>------------------------------<br>  
     * @param id  
     */  
    public void deleteById(String id) {  
    	Orderate orderate = new Orderate(id, null);  
        mongoTemplate.remove(orderate);  
    }
    /**  
     * 按条件删除  
     * <br>------------------------------<br>  
     * @param orderate  
     */  
    public void delete(Orderate orderate) {  
        Criteria criteria = Criteria.where("sum").gt(orderate.getSum());;  
        Query query = new Query(criteria);  
        mongoTemplate.remove(query, Orderate.class);  
    }  
    /**  
     * 删除全部  
     * <br>------------------------------<br>  
     */  
    public void deleteAll() {  
        mongoTemplate.dropCollection(Orderate.class);  
    }  
    /**  
     * 按主键修改,  
     * 如果文档中没有相关key 会新增 使用$set修改器  
     * <br>------------------------------<br>  
     * @param user  
     */  
    public void updateById(Orderate orderate) {  
        Criteria criteria = Criteria.where("id").is(orderate.getId());  
        Query query = new Query(criteria);  
        //Update update = Update.update("age", user.getAge()).set("name", user.getName()); 
        Update update = Update.update("sum", orderate.getSum());
        mongoTemplate.updateFirst(query, update, Orderate.class);  
    }  
    
    /**  
     * 根据主键查询  
     * <br>------------------------------<br>  
     * @param id  
     * @return  
     */  
	public Orderate findById(String id) {  
        return mongoTemplate.findById(id, Orderate.class);  
    }  
	/**  
     * 查询全部  
     * <br>------------------------------<br>  
     * @return  
     */  
	public List<Orderate> findAll() {  
        return mongoTemplate.findAll(Orderate.class);  
    } 
	public List<NetWork> findAll1() {  
        return mongoTemplate.findAll(NetWork.class);  
    } 
	
	public List<NetWork> findAll1s() {  
		DBObject query1 = new BasicDBObject(); //setup the query criteria 设置查询条件
		/*query1.put("id", new BasicDBObject("$in", idList));
		query1.put("time", (new BasicDBObject("$gte", startTime)).append("$lte", endTime));*/
		DBCursor dbCursor =mongoTemplate.getCollection("netWork").find(query1);
		List<NetWork> list=new ArrayList<NetWork>();
		while (dbCursor.hasNext()){
		    DBObject object=dbCursor.next();
		    NetWork te=new NetWork();
		    te.setId(object.get("_id").toString());
		    te.setValue(object.get("value"));
		    list.add(te);
		}
		return list;
    } 
	
	public List<DataTest> findAll2() {  
        return mongoTemplate.findAll(DataTest.class);  
    } 
	
	public List<DataTest> findAll3() { 
		List<DataTest> list=new ArrayList<DataTest>();
		//DBCursor dbCursor =null;
		try {
			DBObject query1 = new BasicDBObject(); //setup the query criteria 设置查询条件
			/*query1.put("id", new BasicDBObject("$in", idList));
			query1.put("time", (new BasicDBObject("$gte", startTime)).append("$lte", endTime));*/
			/*for(int i=0;i<5;i++){
				int start=i*2000;
				DBCursor dbCursor =mongoTemplate.getCollection("dataTest").find(query1).skip(start).sort(new BasicDBObject()).limit(2000);
				//System.out.println(dbCursor.count()); 
				while (dbCursor.hasNext()){
					DBObject object=dbCursor.next();
					DataTest te=new DataTest();
					te.setId(object.get("_id").toString());
					te.setValue(object.get("value"));
					list.add(te);
					
				}
				System.out.println(list.size()); 
				if(dbCursor !=null){
					dbCursor.close();
				}
			}*/
			DBCursor dbCursor =mongoTemplate.getCollection("dataTest").find(query1).limit(300);
			System.out.println(dbCursor.count()); 
			List<Object> lists = new ArrayList<Object>();
			while (dbCursor.hasNext()){
				DBObject object=dbCursor.next();
				/*DataTest te=new DataTest();
				te.setId(object.get("_id").toString());
				te.setValue(object.get("value"));
				list.add(te);*/
				lists.add(object.get("value"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			/*if(dbCursor !=null){
				dbCursor.close();
			}*/
		}
		
		
		return list;
    } 
	
	 /**  
     * 按条件查询, 分页  
     * <br>------------------------------<br>  
     * @param criteriaUser  
     * @param skip  
     * @param limit  
     * @return  
     */  
    public List<Orderate> find(Orderate orderate, int skip, int limit) {  
        Query query = getQuery(orderate);  
        query.skip(skip);  
        query.limit(limit);  
        return mongoTemplate.find(query, Orderate.class);  
    } 
    
    /**  
     *  
     * <br>------------------------------<br>  
     * @param criteriaUser  
     * @return  
     */  
    private Query getQuery(Orderate orderate) {  
        if (orderate == null) {  
        	orderate = new Orderate();  
        }  
        Query query = new Query();  
        if (orderate.getId() != null) {  
            Criteria criteria = Criteria.where("id").is(orderate.getId());  
            query.addCriteria(criteria);  
        }  
        /*if (orderate.getSum() > 0) {  
            Criteria criteria = Criteria.where("sum").gt(orderate.getSum());  
            query.addCriteria(criteria);  
        }*/
        return query;  
    }  
}
