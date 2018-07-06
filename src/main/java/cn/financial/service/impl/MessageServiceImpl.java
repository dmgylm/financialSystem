package cn.financial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.dao.MessageDAO;
import cn.financial.dao.UserRoleDAO;
import cn.financial.model.Message;
import cn.financial.model.Organization;
import cn.financial.model.User;
import cn.financial.model.UserRole;
import cn.financial.service.MessageService;

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

    @Autowired
    private UserRoleDAO userRoleDao;
    
    @Autowired
	private RoleResourceServiceImpl roleResourceServiceImpl;
	
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
        return messageDao.getMessageById(id);
    }

    /**
     * 根据Id更新消息
     */
    @Override
    public Integer updateMessageById(Map<Object, Object> map) {
        return messageDao.updateMessageById(map);
    }

    /**
     * 根据Id删除消息
     */
    @Override
    public Integer deleteMessageById(String id) {
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
	 * 根据用户权限检索对应的消息
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public List<Message> quartMessageByPower(User user) {
		List<UserRole> lur = userRoleDao.listUserRole(user.getName());
		List<Message> lm = new ArrayList<>();
		List<Organization>lo=new ArrayList<>();
		
		for (int i = 0; i < lur.size(); i++) {//查询用户组织结构
			List<JSONObject> uResource = roleResourceServiceImpl.roleResourceList(lur.get(i).getName());
			for (int n = 0; n < uResource.size(); n++) {
				JSONArray ja = JSONArray.parseArray(uResource.get(n).getString("children"));
			
			for (int j = 0; j < ja.size(); j++) {
				JSONObject ob = (JSONObject) ja.get(j);
				if (("0dd6008c6e7f4bce8e1d2ada94341ecf").equals(ob.get("pid"))) {// 是制单员
					List<JSONObject> userOrganization = userOrganizationServiceImpl.userOrganizationList(user.getId()); // 判断
																														// 权限的数据
					for (int k = 0; k < userOrganization.size(); k++) {
						JSONObject obu = (JSONObject) userOrganization.get(k);
						int num=Integer.parseInt(obu.get("orgType").toString());
						System.out.println(num);
						if (2==num) {// 是公司
							Organization org=new Organization();
							org.setId(obu.get("pid").toString());
							if(!lo.contains(org)) {
								Map<Object, Object> map = new HashMap<>();
								map.put("oId", obu.get("pid"));
								List<Message>lms=messageDao.listMessageBy(map);
								if(lms!=null) {
									for (int l = 0; l < lms.size(); l++) {
										if(!lm.contains(lms.get(l))) {
										lm.add(lms.get(l));//添加消息
										}
									}
								}
								lo.add(org);
							}
						}else if(3==num) {
							Organization org = organizationService.getCompanyNameBySon(obu.get("pid").toString());// 获取对应部门的公司
							Organization orgt=new Organization();
							orgt.setId(org.getId());
							if(!lo.contains(orgt)) {
								Map<Object, Object> map = new HashMap<>();
								map.put("oId", org.getId());
								List<Message>lms=messageDao.listMessageBy(map);
								if(lms!=null) {
									for (int l = 0; l < lms.size(); l++) {
										if(!lm.contains(lms.get(l))) {
											lm.add(lms.get(l));//添加消息
										}
									}
								}
								lo.add(org);
							}
							
						}
					}
				}
			}
			
		}

		}
		Map<Object, Object> map = new HashMap<>();
		map.put("uId", user.getId());
		List<Message>lmsg=messageDao.listMessageBy(map);
		for(int i=0;i<lmsg.size();i++) {//查询发送给每个用户的消息
			lm.add(lmsg.get(i));
		}
		
		/* List<Message> resultList = new ArrayList<>();
	        // 根据用户名查询到该用户所对应的角色
	        List<UserRole> listUserRole = userRoleDao.listUserRole(user.getName());
	        // 查询角色对应的资源
	        for (UserRole userRole : listUserRole) {
	            // 根据角色id查询到该角色对应的资源
	            List<RoleResource> listRoleResource = roleResourceDao.listRoleResource(userRole.getrId());
	            // 查询有权限的资源
	            for (RoleResource roleResource : listRoleResource) {
	                // 资源id
	                String resourceId = roleResource.getsId();
	                // 如果存在录入中心资源的id，则说明这个登录人是制单员。
	                if ("0dd6008c6e7f4bce8e1d2ada94341ecf".equals(resourceId)) {
	                    // 查询出所有的消息
	                    List<Message> listAllMessage = messageDao.listAllMessage();
	                    // 根据用户id获取到该用户所对应的组织架构
	                    List<UserOrganization> listUserOrganization = userOrganizationDao
	                            .listUserOrganization(user.getId());
	                    for (Message message : listAllMessage) {
	                        for (UserOrganization userOrganization : listUserOrganization) {
	                            // 若该用户有这条消息的权限，那么就添加到resultList输出
	                            if (userOrganization.getoId().equals(message.getoId())) {
	                                resultList.add(message);
	                            }
	                        }
	                    }
	                } else {
	                    Map<Object, Object> map = new HashMap<>();
	                    map.put("uId", user.getId());
	                    // 是管理层
	                    resultList = messageDao.listMessageBy(map);
	                }
	            }
	        }
	        // 去掉可能重复的数据
	        for (int i = 0; i < resultList.size() - 1; i++) {
	            for (int j = resultList.size() - 1; j > i; j--) {
	                if (resultList.get(j).equals(resultList.get(i))) {
	                    resultList.remove(j);
	                }
	            }
	        }
	        // 如果是公司及公司以下的部门，则能看到消息;如果是公司以上级别的不能返回消息
	        for (int i = resultList.size()-1; i >=0; i--) {
	            Map<Object, Object> map = new HashMap<>();
	            map.put("id", resultList.get(i).getoId());
	            List<Organization> orga = organizationDao.listOrganizationBy(map);
	            if (!CollectionUtils.isEmpty(orga)) {
	                if (orga.get(0).getOrgType() == 1 || orga.get(0).getOrgType() == 4) {
	                    resultList.remove(i);
	                }
	            }
	        }
	        // 按照创建日期降序排序
	        Collections.sort(resultList, new Comparator<Message>() {
	            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	            @Override
	            public int compare(Message o1, Message o2) {
	                long time1;
	                try {
	                    time1 = df.parse(o1.getCreateTime().toString()).getTime();
	                    long time2 = df.parse(o2.getCreateTime().toString()).getTime();
	                    if (time1 < time2) {
	                        return 1;
	                    } else if (time1 == time2) {
	                        return 0;
	                    } else {
	                        return -1;
	                    }
	                } catch (ParseException e) {
	                    e.printStackTrace();
	                }
	                return 0;
	            }
	        });
	        // 分页
	        List<Message> result = new ArrayList<>();
	        int start = resultList.size() >= (page - 1) * pageSize ? (page - 1) * pageSize : -1;
	        int end = resultList.size() >= pageSize * page ? pageSize * page : resultList.size();
	        if (start != -1) {
	            for (int i = start; i < end; i++) {
	                result.add(resultList.get(i));
	            }
	        }
	        resultJsonObject.put("count", resultList.size());
	        resultJsonObject.put("data", result);
	       */
		System.out.println(lm);
		return lm;
	}

}
