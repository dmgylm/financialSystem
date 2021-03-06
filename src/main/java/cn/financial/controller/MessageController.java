package cn.financial.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Message;
import cn.financial.model.User;
import cn.financial.model.response.ListMessage;
import cn.financial.model.response.MessageInfo;
import cn.financial.model.response.ResultUtils;
import cn.financial.model.response.UnreadInfo;
import cn.financial.service.MessageService;
import cn.financial.service.impl.CapitalServiceImpl;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.webSocket.FinancialSocketHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 消息相关操作
 * 
 * @author zlf 2018/03/13
 *
 */
@Api(tags = "消息中心接口")
@Controller
@RequestMapping("/message")
public class MessageController {
	
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private CapitalServiceImpl capitalServiceImpl;
    
    @Bean
	public FinancialSocketHandler financialWebSocketHandler() {
		
		return new FinancialSocketHandler();
		
	}
    
    public void sendSocketInfo(String unread, String sessionId) {
    	
    	JSONObject jsonObject = new JSONObject();
        jsonObject.put("unread",unread);
        financialWebSocketHandler().sendMessageToUser("MessageSocketServerInfo;JSESSIONID="+sessionId, new TextMessage(jsonObject.toString()),unread);
    	
    }
    
	public void sendSocketMessageInfo(String unread, String userId) {
	    	
	    	JSONObject jsonObject = new JSONObject();
	        jsonObject.put("unread",unread);
	        System.out.println("userId="+userId+"]");
	        financialWebSocketHandler().sendMessageToUser("userId="+userId+"]", new TextMessage(jsonObject.toString()),unread);
	    	
	    }
    
    
    protected Logger logger = LoggerFactory.getLogger(MessageController.class);
    
    
    /**
     * 根据用户权限展示相应的消息
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @RequiresPermissions("message:view")
    @ApiOperation(value="查询消息",notes="根据用户权限查询相应的消息",response = ListMessage.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="pageSize",value="一页显示的数据",dataType="int", paramType = "query"),
        @ApiImplicitParam(name="page",value="显示第几页",dataType="int", paramType = "query"),
        @ApiImplicitParam(name="status",value="消息状态(0:未读,1:已读,2重要消息)",dataType="int",paramType="query")})
    public ListMessage listMessage(HttpServletRequest request, HttpServletResponse response) {
        ListMessage result = new ListMessage();
        
        try {
        	Map<Object, Object> pagingMap = new HashMap<Object, Object>();
            User user = (User) request.getAttribute("user");
            pagingMap.put("pageSize", request.getParameter("pageSize"));
            pagingMap.put("page", request.getParameter("page"));
            pagingMap.put("status", request.getParameter("status"));
            List<Message> lm = messageService.quartMessageByPower(user,pagingMap);//获取消息集合
            
            Map<Object, Object> map = new HashMap<Object, Object>();
    		map.put("pageSize", Message.PAGESIZE);
    		map.put("start", 0);
    		map.put("status", request.getParameter("status"));
            Integer total = messageService.quartMessageByPower(user,map).size();//用户总的消息条数
            
            if (lm.size() >= 0) {
            	result.setTotal(total);
            	result.setData(lm);
                ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
            } else {
            	ElementXMLUtils.returnValue((ElementConfig.MESSAGE_LIST_NULL),result);
            }
        } catch (Exception e) {
        	ElementXMLUtils.returnValue((ElementConfig.RUN_FAILURE),result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 新增 消息
     * 
     * @param request
     * @param response
     * @return
     *//*
    public Map<String, Object> saveMessage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            String statusStr = request.getParameter("status");
            String themeStr = request.getParameter("theme");
            String content = request.getParameter("content");
            int status = 0;
            int theme = 0;
            if (content != null && !"".equals(content)) {
                content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
            }
            if (statusStr != null && !"".equals(statusStr)) {
                status = Integer.parseInt(statusStr);
            }
            if (themeStr != null && !"".equals(themeStr)) {
                theme = Integer.parseInt(themeStr);
            }
            Message message = new Message();
            message.setId(UuidUtil.getUUID());// 消息id
            message.setStatus(status);// 消息状态
            message.setTheme(theme);// 消息主题
            message.setContent(content);// 消息内容
            message.setuId(request.getParameter("uId"));// 消息来源
            message.setsuId(request.getParameter("suId"));//消息来源
            Integer i = messageService.saveMessage(message);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "新增成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "新增失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }*/

    /**
     * 根据消息状态展示消息列表
     * 
     * @param request
     * @param response
     * @throws ParseException 
     */
//    @ResponseBody
//    @RequestMapping(value = "/listBy", method = RequestMethod.POST)
//    @RequiresPermissions("message:view")
//    @ApiOperation(value="查询消息",notes="根据消息状态查询相应的消息",response = ListMessage.class)
//    @ApiImplicitParams({
//        @ApiImplicitParam(name="pageSize",value="一页显示的数据",dataType="int", paramType = "query"),
//        @ApiImplicitParam(name="page",value="显示第几页",dataType="int", paramType = "query"),
//        @ApiImplicitParam(name="status",value="消息状态(0:未读,1:已读,2重要消息)",dataType="int",paramType="query",required = true)})
//    public ListMessage listMessageBy(HttpServletRequest request, HttpServletResponse response){
//    	ListMessage result = new ListMessage();
//        Map<Object, Object> map = new HashMap<Object, Object>();
//        List<Message> list = new ArrayList<Message>();
//        try {
//        		User user = (User) request.getAttribute("user");
//        		map.put("pageSize", request.getParameter("pageSize"));
//                map.put("page", request.getParameter("page"));
//                map.put("status", request.getParameter("status"));
//        		
//                list = messageService.quartMessageByPower(user, map);
//                Integer total = list.size();
//                
//                result.setData(list);
//                result.setTotal(total);
//                
//                ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
//        } catch (Exception e) {
//        	ElementXMLUtils.returnValue((ElementConfig.RUN_ERROR),result);
//            this.logger.error(e.getMessage(), e);
//        }
//        return result;
//    }

    /**
     * 根据条件查询 消息
     * 
     * @param request
     * @param response
     * @return
     */
    /*
    public Map<String, Object> listMessageBy(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String statusStr = request.getParameter("status");
        String themeStr = request.getParameter("theme");
        String content = request.getParameter("content");
        String createTime = request.getParameter("createTime");
        String updateTime = request.getParameter("updateTime");
        Date createTimeOfDate = null;
        Date updateTimeOfDate = null;
        int status = 0;
        int theme = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (content != null && !"".equals(content)) {
                content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
            }
            if (createTime != null && !"".equals(createTime)) {
                createTimeOfDate = dateFormat.parse(createTime);
            }
            if (updateTime != null && !"".equals(updateTime)) {
                updateTimeOfDate = dateFormat.parse(updateTime);
            }
            if (statusStr != null && !"".equals(statusStr)) {
                status = Integer.parseInt(statusStr);
            }
            if (themeStr != null && !"".equals(themeStr)) {
                theme = Integer.parseInt(themeStr);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("id", request.getParameter("id"));// 消息id
            map.put("status", status);// 消息状态
            map.put("theme", theme);// 消息主题
            map.put("content", content);// 消息内容
            map.put("uId", request.getParameter("uId"));// 消息来源
            map.put("isTag", request.getParameter("isTag"));// 是否标注(0未标注；1标注)
            map.put("createTime", createTimeOfDate);// 创建时间
            map.put("updateTime", updateTimeOfDate);// 更新时间
            List<Message> list = messageService.listMessageBy(map);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("data", list);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }*/

    /**
     * 根据ID查询消息
     * 
     * @param request
     * @param id
     *            要查询消息表的ID（required = true，必须存在）
     * @return
     */
    @RequiresPermissions("message:view")
    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ApiOperation(value="根据id查询消息",notes="根据id查询消息",response = MessageInfo.class)
    @ApiImplicitParams({@ApiImplicitParam(name="id",value="消息id",dataType="string", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 251, message = "消息id为空")})
    @ResponseBody
    public MessageInfo getMessageById(HttpServletRequest request,String id) {
        MessageInfo result = new MessageInfo();
        
        try {
            Message message = messageService.getMessageById(id);
            if(message == null){
                ElementXMLUtils.returnValue(ElementConfig.MESSAGE_ID_NULL,result);
                return result;
            }
            result.setData(message);
            ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
        } catch (Exception e) {
        	ElementXMLUtils.returnValue((ElementConfig.RUN_ERROR),result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据条件修改消息表,这里是根据id来修改其他项,所以map中必须包含id
     * 
     * @param request
     * @param response
     * @param id
     *            消息id（required = true，必须存在）
     * @return
     */
    @RequiresPermissions("message:sign")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    @ApiOperation(value="修改消息",notes="根据id修改消息",response = UnreadInfo.class)
    @ApiImplicitParams({
    	 @ApiImplicitParam(name="status",value="消息状态(0:未读,1:已读)",dataType="int", paramType = "query"),
         @ApiImplicitParam(name="isTag",value="是否标注(0:未标注,1:已标注)",dataType="int", paramType = "query"),
         @ApiImplicitParam(name="id",value="消息id",dataType="string", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 251, message = "消息id为空")})
    @ResponseBody
    public UnreadInfo updateMessageById(HttpServletRequest request, HttpServletResponse response, String id) {
    	UnreadInfo result = new UnreadInfo();
        
        try {
           
            Map<Object, Object> map = new HashMap<Object, Object>();
            map.put("status", request.getParameter("status"));
            map.put("isTag", request.getParameter("isTag"));
            map.put("id", id);
        	Integer i = messageService.updateMessageById(map);
        	
        	if(i == null) {
        		ElementXMLUtils.returnValue(ElementConfig.MESSAGE_ID_NULL,result);
                return result;
        	}
            if (Integer.valueOf(1).equals(i)) {
            	
            	User user = (User) request.getAttribute("user");
        		Map<Object, Object> map1 = new HashMap<Object, Object>();
        		map1.put("pageSize", Message.PAGESIZE);
        		map1.put("start", 0);
        		map1.put("status", "0");
                Integer unreadMessage = messageService.quartMessageByPower(user,map1).size();
                result.setData(unreadMessage);
            	ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
            } else {
            	ElementXMLUtils.returnValue((ElementConfig.RUN_ERROR),result);
            }
        } catch (Exception e) {
        	ElementXMLUtils.returnValue((ElementConfig.RUN_FAILURE),result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据ID删除消息表信息
     * 
     * @param request
     * @param id
     *            传入的消息表id（required = true，必须存在）
     * @return
     */
    @RequiresPermissions("message:sign")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    @ApiOperation(value="删除消息",notes="根据id删除消息",response = ResultUtils.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name="id",value="消息id",dataType="string", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 251, message = "消息id为空")})
    @ResponseBody
    public ResultUtils deleteMessageById(HttpServletRequest request, String id) {
        ResultUtils result = new ResultUtils();
        
        try {
            Integer i = messageService.deleteMessageById(id);
            if(i == null) {
        		ElementXMLUtils.returnValue(ElementConfig.MESSAGE_ID_NULL,result);
                return result;
        	}
            if (Integer.valueOf(1).equals(i)) {
            	ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
            } else {
            	ElementXMLUtils.returnValue((ElementConfig.RUN_ERROR),result);
            }
        } catch (Exception e) {
        	ElementXMLUtils.returnValue((ElementConfig.RUN_FAILURE),result);
            this.logger.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 汇总表生成时给指定用户发送消息
     * 
     * @param request
     * @param id
     *            传入的消息表id（required = true，必须存在）
     * @return
     */
//    public Map<Object, Object> saveMessageByUser(HttpServletRequest request,HttpServletResponse response) {
//        Map<Object, Object> dataMap = new HashMap<Object, Object>();
//        try {
//				Message message = new Message();
//				message.setId(UuidUtil.getUUID());
//				message.setStatus(0);
//				message.setTheme(1);
//				message.setContent("汇总损益表已生成");
//				message.setuId(request.getParameter("uid"));//发送指定人的id
//				message.setIsTag(0);
//				message.setsName("系统");
//				message.setFileurl(request.getParameter("fileUrl"));//汇总表文件的路径
//				Integer i1 = messageService.saveMessage(message);
//            if (Integer.valueOf(1).equals(i1)) {
//            	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
//            } else {
//            	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
//            }
//            
//            String unread = String.valueOf(listUnreadMessage(request, response));//获取未读消息条数
//            sendSocketInfo(unread);
//            
//        } catch (Exception e) {
//        	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
//            this.logger.error(e.getMessage(), e);
//        }
//        return dataMap;
//    }
    
    /**
     * 查询未读消息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listUnread", method = RequestMethod.POST)
    @ApiOperation(value="查询未读消息",notes="根据登陆用户查询未读消息",response = UnreadInfo.class)
    public UnreadInfo listUnreadMessage(HttpServletRequest request, HttpServletResponse response){
    	
    	UnreadInfo result = new UnreadInfo();
    	
    	try {
    		User user = (User) request.getAttribute("user");
    		Map<Object, Object> map = new HashMap<Object, Object>();
    		map.put("pageSize", Message.PAGESIZE);
    		map.put("start", 0);
    		map.put("status", "0");
            Integer unreadMessage = messageService.quartMessageByPower(user,map).size();
            result.setData(unreadMessage);
            ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
    	}catch (Exception e) {
    		ElementXMLUtils.returnValue((ElementConfig.RUN_ERROR),result);
    		this.logger.error(e.getMessage() , e);
		}
        
        return result;
    }
    
    /**
     * 根据地址下载文件
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/getFile", method = RequestMethod.GET)
    @ApiOperation(value="下载文件",notes="根据地址下载文件",response = ResultUtils.class)
    @ApiImplicitParams({@ApiImplicitParam(name="id",value="消息id",dataType="string", paramType = "query", required = true)})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 400, message = "失败"),
        @ApiResponse(code = 500, message = "系统错误"),@ApiResponse(code = 251, message = "消息id为空")})
    @RequiresPermissions("message:download")
    public ResultUtils getFile(HttpServletRequest request, HttpServletResponse response) {
    	ResultUtils result = new ResultUtils();
    	Map<Object, Object> map = new HashMap<Object, Object>();
    	try {
    			if(request.getParameter("id") == null || request.getParameter("id").equals("")) {
    				ElementXMLUtils.returnValue(ElementConfig.MESSAGE_ID_NULL,result);
                    return result;
    			}
    			Message m = messageService.getMessageById(request.getParameter("id"));
            	String fileURL = m.getFileurl();
            	File file=new File(fileURL);
            	boolean b = capitalServiceImpl.export(request, response, file);
            	if(b) {
            		map.put("id", request.getParameter("id"));
            		map.put("status", 1);
            		Integer i = messageService.updateMessageById(map);
            		if(Integer.valueOf(1).equals(i)) {
            			ElementXMLUtils.returnValue((ElementConfig.RUN_SUCCESSFULLY),result);
            		}else {
            			ElementXMLUtils.returnValue((ElementConfig.RUN_ERROR),result);
            		}
            	}else {
            		ElementXMLUtils.returnValue((ElementConfig.GETFILE_FAIL),result);
            	}
    	}catch (Exception e) {
    		e.printStackTrace();
    		ElementXMLUtils.returnValue((ElementConfig.RUN_FAILURE),result);
		}
    	
    	return result;
    }   
    
}
