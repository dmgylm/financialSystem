package cn.financial.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.financial.model.Message;
import cn.financial.model.User;
import cn.financial.service.MessageService;
import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
import cn.financial.util.UuidUtil;

/**
 * 消息相关操作
 * 
 * @author zlf 2018/03/13
 *
 */
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    protected Logger logger = LoggerFactory.getLogger(MessageController.class);

    /**
     * 新增 消息
     * 
     * @param request
     * @param response
     * @return
     *//*
    @RequiresPermissions("message:create")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
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
    @RequiresPermissions("capital:view")
    @RequestMapping(value = "/listBy", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listMessageBy(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<Object, Object> map = new HashMap<Object, Object>();
        Map<Object, Object> mapn = new HashMap<Object, Object>();
        List<Message> list = null;
        List<Message> listm=null;
        //Date createTimeOfDate = null;
        //Date updateTimeOfDate = null;
        int unreadmessage=0;
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        if(null != request.getParameter("theme") && !"".equals(request.getParameter("theme"))) {
        	map.put("theme",Integer.parseInt(request.getParameter("theme")));// 消息主题
        }
        if(null != request.getParameter("content") && !"".equals(request.getParameter("content"))) {
        	map.put("content", request.getParameter("content"));// 消息内容
        }
        if(null != request.getParameter("createTime") && !"".equals(request.getParameter("createTime"))) {
        	//createTimeOfDate = dateFormat.parse(request.getParameter("createTime"));
        	//map.put("createTime", dateFormat.parse(request.getParameter("createTime")));// 创建时间
        	map.put("createTime", request.getParameter("createTime"));
        }
        if(null != request.getParameter("updateTime") && !"".equals(request.getParameter("updateTime"))) {
        	map.put("updateTime", request.getParameter("updateTime"));
        	//updateTimeOfDate = dateFormat.parse(request.getParameter("updateTime"));
        	//map.put("updateTime", dateFormat.parse(request.getParameter("updateTime")));// 更新时间
        }
   /*     if(null != request.getParameter("id") && !"".equals(request.getParameter("id"))) {
        	map.put("id", request.getParameter("id"));// 消息id
        }*/
        Integer pageSize=0;
        if(request.getParameter("pageSize")!=null && !request.getParameter("pageSize").equals("")){
            pageSize=Integer.parseInt(request.getParameter("pageSize"));
            map.put("pageSize",pageSize);
        }
        Integer start=0;
        if(request.getParameter("page")!=null && !request.getParameter("page").equals("")){
            start=pageSize * (Integer.parseInt(request.getParameter("page")) - 1);
            map.put("start",start);
        }
        try {
        		User user = (User) request.getAttribute("user");
        		map.put("uId", user.getId());
        		mapn.put("uId", user.getId());
        		if (null != request.getParameter("status") && !"".equals(request.getParameter("status"))) {
                	if(request.getParameter("status") == "2") {
                		map.put("isTag", "1");
                	}else {
                		map.put("status", request.getParameter("status"));
                	/*	if(null != request.getParameter("isTag") && !"".equals(request.getParameter("isTag"))) {
                			map.put("isTag", request.getParameter("isTag"));// 是否标注(0未标注；1标注)
                		}*/
                	}
                }
        		
                list = messageService.listMessage(map);
                listm= messageService.listMessage(mapn);
                for(int k=0;k<listm.size();k++) {
                    if(listm.get(k).getStatus()==0) {
                    	unreadmessage+=1;
                    }
                }
                dataMap.put("resultstatus", unreadmessage);//未读的条数
                dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
                dataMap.put("resultData", list);
        } catch (Exception e) {
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据条件查询 消息
     * 
     * @param request
     * @param response
     * @return
     */
    /*@RequiresPermissions("capital:view")
    @RequestMapping(value = "/listBy", method = RequestMethod.POST)
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
            dataMap.put("resultData", list);
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
    @RequestMapping(value = "/getbyid", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getMessageById(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Message message = messageService.getMessageById(id);
            dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            dataMap.put("resultData", message);
        } catch (Exception e) {
        	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
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
    @RequiresPermissions("message:update")
    @RequestMapping(value = "/updatebyid", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateMessageById(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "id", required = true) String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
           
            Map<Object, Object> map = new HashMap<>();
            if(request.getParameter("status")!=null) {
            		map.put("status", request.getParameter("status"));
            }
            if(request.getParameter("isTag")!=null) {
            		map.put("isTag", request.getParameter("isTag"));
            }
            map.put("id", id);
        	Integer i = messageService.updateMessageById(map);
            if (Integer.valueOf(1).equals(i)) {
            	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            } else {
            	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
        	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }

    /**
     * 根据ID删除消息表信息
     * 
     * @param request
     * @param id
     *            传入的消息表id（required = true，必须存在）
     * @return
     */
    @RequiresPermissions("message:update")
    @RequestMapping(value = "/deletebyid", method = RequestMethod.POST)
    @ResponseBody
    public Map<Object, Object> deleteMessageById(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = messageService.deleteMessageById(id);
            if (Integer.valueOf(1).equals(i)) {
            	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            } else {
            	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
        	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
    /**
     * 汇总表生成时给指定用户发送消息
     * 
     * @param request
     * @param id
     *            传入的消息表id（required = true，必须存在）
     * @return
     */
    @RequiresPermissions("message:save")
    @RequestMapping(value = "/saveMessageByUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<Object, Object> saveMessageByUser(HttpServletRequest request) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
				Message message = new Message();
				message.setId(UuidUtil.getUUID());
				message.setStatus(0);
				message.setTheme(1);
				message.setContent("汇总损益表已生成");
				message.setuId(request.getParameter("uid"));//发送指定人的id
				message.setIsTag(0);
				message.setsName("系统");
				message.setFileurl(request.getParameter("fileUrl"));//汇总表文件的路径
				Integer i1 = messageService.saveMessage(message);
            if (Integer.valueOf(1).equals(i1)) {
            	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_SUCCESSFULLY));
            } else {
            	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_ERROR));
            }
        } catch (Exception e) {
        	dataMap.putAll(ElementXMLUtils.returnValue(ElementConfig.RUN_FAILURE));
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
}
