package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.financial.controller.MessageController;
import cn.financial.dao.MessageDAO;
import cn.financial.model.DataModule;
import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.UserOrganization;
import cn.financial.service.MessageService;
import cn.financial.util.UuidUtil;

/**
 * 消息Service业务实现类
 * 
 * @author zlf 2018/03/13
 *
 */
@Service("MessageServiceImpl")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAO messageDao;

//    @Autowired
//    private UserRoleDAO userRoleDao;
    
//    @Autowired
//	private RoleResourceServiceImpl roleResourceServiceImpl;
	
	@Autowired
	private UserOrganizationServiceImpl userOrganizationServiceImpl;
	
	@Autowired
	private OrganizationServiceImpl organizationService;


    /**
     * 新增 消息 节点信息
     */
    @Override
    public Integer saveMessage(Message message) {
        return messageDao.saveMessage(message);
    }

    /**
     * 根据消息状态展示消息列表
     */
    @Override
    public List<Message> listMessage(Map<Object, Object> map) {
        return messageDao.listMessage(map);
    }

    /**
     * 根据条件查询消息
     */
    @Override
    public List<Message> listMessageBy(Map<Object, Object> map) {
        return messageDao.listMessageBy(map);
    }

    /**
     * 根据ID查询消息
     */
    @Override
    public Message getMessageById(String id) {
    	if(id == null || id.equals("")){
            return null;  
        }
        return messageDao.getMessageById(id);
    }

    /**
     * 根据Id更新消息
     */
    @Override
    public Integer updateMessageById(Map<Object, Object> map) {
    	
    	Map<Object, Object> um = new HashMap<>();
    	
    	if(map.get("status") != null) {
    		um.put("status", map.get("status"));
    	}
    	if(map.get("isTag") != null) {
    		um.put("isTag", map.get("isTag"));
    	}
    	if(map.get("id") == null || map.get("id").equals("")) {
    		return null;
    	}else {
    		um.put("id", map.get("id"));
    	}
    	
        return messageDao.updateMessageById(um);
    }

    /**
     * 根据Id删除消息
     */
    @Override
    public Integer deleteMessageById(String id) {
    	if(id == null || id.equals("")){
            return null;  
        }
        return messageDao.deleteMessageById(id);
    }

    /**
     * 检索所有的消息
     */
    @Override
    public List<Message> listAllMessage() {
        List<Message> list = messageDao.listAllMessage();
        return list;
    }
    
    /**
     * 汇总表生成时给指定用户发送消息
     * 
     * @param user		登陆的用户
     * @param fileUrl	汇总表文件的路径
     */
    @Override
    public Integer saveMessageByUser(User user, String fileUrl, String url, String reportType) {
    	
    	Integer n = 0;
    	MessageController mc = new MessageController();
        try {
	        	Message message = new Message();
				message.setId(UuidUtil.getUUID());
				message.setStatus(0);
				message.setTheme(2);
				if(DataModule.REPORT_TYPE_BUDGET.equals(reportType)){
					message.setContent("预算汇总表已生成");
				}
				if(DataModule.REPORT_TYPE_PROFIT_LOSS.equals(reportType)){
					message.setContent("损益汇总表已生成");
				}
				if(DataModule.REPORT_TYPE_BUDGET_SUMMARY.equals(reportType)){
					message.setContent("预算简易汇总表已生成");
				}
				if(DataModule.REPORT_TYPE_PROFIT_LOSS_SUMMARY.equals(reportType)){
					message.setContent("损益简易汇总表已生成");
				}
				if(DataModule.REPORT_TYPE_ASSESSMENT.equals(reportType)){
					message.setContent("考核汇总表已生成");
				}
				if(DataModule.REPORT_TYPE_TAX.equals(reportType)){
					message.setContent("税金汇总表已生成");
				}
//				message.setContent("汇总损益表已生成");
				message.setuId(user.getId());//发送指定人的id
				message.setIsTag(0);
				message.setsName("系统");
				message.setFileurl(fileUrl);//汇总表文件的路径
				n = saveMessage(message);
				
				Map<Object, Object> map = new HashMap<>();
	    		map.put("pageSize", Message.PAGESIZE);
	    		map.put("start", 0);
				List<Message> list = quartMessageByPower(user,map);
	            String sessionId = url.substring(url.lastIndexOf("=")+1);
	            mc.sendSocketInfo(String.valueOf(list.size()), sessionId);
            
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return n;
    }
    
	/**
	 * 根据用户权限检索对应的消息
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public List<Message> quartMessageByPower(User user,Map<Object, Object> pagingMap) {
		
//		final Integer TWO = 2;
//		final Integer THREE = 3;
		// 默认一页显示的数据
		Integer pageSize = 10;
        if (null != pagingMap.get("pageSize") && !"".equals(pagingMap.get("pageSize"))) {
            pageSize = Integer.parseInt(pagingMap.get("pageSize").toString());
        }
        // 默认显示第一页
        Integer start = 0;
        if (null != pagingMap.get("page") && !"".equals(pagingMap.get("page"))) {
            start = pageSize * (Integer.parseInt(pagingMap.get("page").toString()) - 1);
        }
		
//		List<UserRole> lur = userRoleDao.listUserRole(user.getName(), null);
		List<Message> lm = new ArrayList<Message>();
		List<String> his = new ArrayList<String>();
		Map<Object, Object> filter = new HashMap<Object, Object>();
		
		String uId = user.getId();
		
		List<JSONObject> list = userOrganizationServiceImpl.userOrganizationList(uId);//根据用户ID查询关联的组织结构
		
		UserOrganization uo = userOrganizationServiceImpl.maxOrganizations(uId);//根据用户ID获得最高组织节点
		
		String[] strArray;
		
		Integer orgType = Integer.valueOf(uo.getOrgType());
		if(orgType == 1) {//如果是汇总级别，根据oid判断其上是否有公司
			Organization org = organizationService.getCompanyNameBySon(uo.getoId());
			if(org == null) {
				his.clear();
			}else {
				for(int i=0; i<list.size(); i++) {
					if(Integer.valueOf(list.get(i).getString("orgType")) != 2) {//不是公司级别
						Organization orga = organizationService.getCompanyNameBySon(list.get(i).getString("pid"));
						strArray = orga.getHis_permission().split(",");
						his.addAll(Arrays.asList(strArray));
					}else {
						strArray = list.get(i).getString("his_permission").split(",");
						his.addAll(Arrays.asList(strArray));
					}
				}
			}
			
		}
		if(orgType == 2 || orgType == 3) {//如果是公司或部门级别，获取code集合
			for(int i=0; i<list.size(); i++) {
				if(Integer.valueOf(list.get(i).getString("orgType")) != 2) {//不是公司级别
					Organization orga = organizationService.getCompanyNameBySon(list.get(i).getString("pid"));
					strArray = orga.getHis_permission().split(",");
					his.addAll(Arrays.asList(strArray));
				}else {
					strArray = list.get(i).getString("his_permission").split(",");
					his.addAll(Arrays.asList(strArray));
				}
			}
		}
		if(orgType == 4) {//如果是公司以上，清空集合
			his.clear();
		}
		
		String code[] = new String[his.size()];
		for(int j=0; j<his.size(); j++) {
			code[j] = his.get(j);
			System.out.println(code[j]);
		}
 		
//		for (int i = 0; i < lur.size(); i++) {//查询用户组织结构
//			List<JSONObject> uResource = roleResourceServiceImpl.roleResourceList(lur.get(i).getName());
//			for (int n = 0; n < uResource.size(); n++) {
//				JSONArray ja = JSONArray.parseArray(uResource.get(n).getString("children"));
//			
//				for (int j = 0; j < ja.size(); j++) {
//					JSONObject ob = (JSONObject) ja.get(j);
//					if (("0dd6008c6e7f4bce8e1d2ada94341ecf").equals(ob.get("pid"))) {// 是制单员
//						List<JSONObject> userOrganization = userOrganizationServiceImpl.userOrganizationList(user.getId()); // 判断
//																															// 权限的数据
//						for (int k = 0; k < userOrganization.size(); k++) {
//							JSONObject obu = (JSONObject) userOrganization.get(k);
//							int num=Integer.parseInt(obu.get("orgType").toString());
//							if (TWO == num) {// 是公司
//								Organization org=new Organization();
//								org.setId(obu.get("pid").toString());
//								if(!lo.contains(org)) {
//									oIdList.add((String) obu.get("pid"));
//									lo.add(org);
//								}
//							}else if(THREE == num) {
//								Organization org = organizationService.getCompanyNameBySon(obu.get("pid").toString());// 获取对应部门的公司
//								Organization orgt=new Organization();
//								if(org != null) {
//									orgt.setId(org.getId());
//									if(!lo.contains(orgt)) {
//										oIdList.add(org.getId());
//										lo.add(org);
//									}
//								}else {
//									continue;
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		if(pagingMap.get("status")!=null && !"".equals(pagingMap.get("status"))) {
			if(code.length>0) {
				filter.put("code", code);
			}
			filter.put("uId", user.getId());
			filter.put("pageSize", pageSize);
			filter.put("start", start);
			if(pagingMap.get("status").equals("2")) {
				filter.put("isTag", 1);
			}else {
				filter.put("status", pagingMap.get("status"));
			}
			lm = messageDao.listMessageBy(filter);
		}else {
			if(code.length>0) {
				filter.put("code", code);
			}
			filter.put("uId", user.getId());
			filter.put("pageSize", pageSize);
			filter.put("start", start);
			lm = messageDao.listMessageBy(filter);
		}
		return lm;
	}
}