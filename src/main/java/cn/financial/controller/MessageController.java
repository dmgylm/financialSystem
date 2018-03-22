package cn.financial.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import cn.financial.model.Message;
import cn.financial.service.MessageService;
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
     */
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
    }

    /**
     * 根据消息状态展示消息列表
     * 
     * @param request
     * @param response
     */
    @RequiresPermissions("capital:view")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Map<String, Object> listMessage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<Object, Object> map = new HashMap<Object, Object>();
        try {
        	if (request.getParameter("status")!=null) {
        		
        		String status =request.getParameter("status");
        		if(status=="2") {
        			map.put("isTag", '1');
        		}else {
        			map.put("status", status);
        		}
        	}
            List<Message> list = messageService.listMessage(map);
            int unreadmessage=0;
            for(int i=0;i<list.size();i++) {
            	if(list.get(i).getStatus()==0) {
            		unreadmessage+=1;
            	}
            }
            dataMap.put("resultstatus", unreadmessage);//未读的条数
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", list);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
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
    @RequiresPermissions("capital:view")
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
    }

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
    public Map<String, Object> getMessageById(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            Message message = messageService.getMessageById(id);
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询成功!");
            dataMap.put("resultData", message);
        } catch (Exception e) {
            dataMap.put("resultCode", 200);
            dataMap.put("resultDesc", "查询失败!");
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
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "修改成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "修改失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
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
    public Map<Object, Object> deleteMessageById(HttpServletRequest request,
            @RequestParam(value = "id", required = true) String id) {
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            Integer i = messageService.deleteMessageById(id);
            if (Integer.valueOf(1).equals(i)) {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "删除成功!");
            } else {
                dataMap.put("resultCode", 200);
                dataMap.put("resultDesc", "删除失败!");
            }
        } catch (Exception e) {
            dataMap.put("resultCode", 500);
            dataMap.put("resultDesc", "服务器异常!");
            this.logger.error(e.getMessage(), e);
        }
        return dataMap;
    }
}
