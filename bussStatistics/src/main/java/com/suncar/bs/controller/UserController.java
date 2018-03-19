package com.suncar.bs.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suncar.bs.services.UserService;
import com.suncar.common.util.FileUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	public UserService userService;

	// 跳转到登录页面
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String userLogin(HttpServletRequest request, HttpSession session) {
		return "login";
	}

	// 跳转到锁屏页面
	@RequestMapping(value = "/lock", method = RequestMethod.GET)
	public String userLock(HttpServletRequest request, HttpSession session) {
		return "lock_screen";
	}

	// 跳转到主页面 main
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String userMain(HttpServletRequest request, HttpSession session) {
		return "main";
	}

	// 跳转到播控分发页面 distribution
	@RequestMapping(value = "/distribution", method = RequestMethod.GET)
	public String userDistribution(HttpServletRequest request,
			HttpSession session) {
		return "distribution";
	}
	
	// 跳转到播控分发页面 voiceControl
	@RequestMapping(value = "/userVoiceControl", method = RequestMethod.GET)
	public String userVoiceControl(HttpServletRequest request,
			HttpSession session) {
		return "voiceControl";
	}
	
	// 跳转到定时设置页面 quartzControl
	@RequestMapping(value = "/quartzControl", method = RequestMethod.GET)
	public String quartzControl(HttpServletRequest request,
			HttpSession session) {
		return "quartz";
	}
	
	//跳转到屏板设置页面
	@RequestMapping(value="/screenControl", method = RequestMethod.GET)
	public String screenControl(HttpServletRequest request,
			HttpSession session) {
		return "largescreen";
	}

	// 用户登录
	@RequestMapping(value = "login1", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		String path = //System.getProperty("user.dir");
				this.getClass().getClassLoader().getResource("data/user.json").getPath();
		File file = new File(path);// + "/src/main/resources/data/user.json");
		boolean flag = userService.CheckLogin(username, password, file);
		if (flag) {
			dataMap.put("resultCode", "200");
			dataMap.put("reason", "登陆成功！");
			request.getSession().setAttribute("username", username);
		} else {
			dataMap.put("resultCode", "400");
			dataMap.put("reason", "用户名或者密码不匹配！");
		}
		FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
	}

	// 注册新用户
	@RequestMapping("register")
	public void register(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		String path = //System.getProperty("user.dir");
				this.getClass().getClassLoader()
				.getResource("data/user.json").getPath();
		File file = new File(path); //+ "/src/main/resources/data/user.json");
		boolean flag = userService.addUser(username, password, file);
		if (username != null && username != "" && password != null
				&& password != "") {
			if (flag) {
				dataMap.put("resultCode", "200");
				dataMap.put("reason", "添加用户成功！");
			} else {
				dataMap.put("resultCode", "400");
				dataMap.put("reason", "添加用户失败！");
			}
		}
		FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
	}

	// 退出登录
	@RequestMapping(value = "signOut")
	public String signOut(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("username");
		return "redirect:login";
	}

	// 锁屏界面的登陆,先判断session中是否还存在username,存在则输入密码登录，否则跳转到登陆界面
	@RequestMapping(value = "lockLogin", method = RequestMethod.POST)
	public void lockLogin(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String usernameBySession = (String) request.getSession().getAttribute(
				"username");
		System.out.println("usernameBySession："+usernameBySession);
		if (usernameBySession != null && usernameBySession != "") {
			String path = //System.getProperty("user.dir");
					this.getClass().getClassLoader().getResource("data/user.json").getPath();
			File file = new File(path);// + "/src/main/resources/data/user.json");
			String password = request.getParameter("password");
			boolean flag = userService.CheckLogin(usernameBySession, password, file);
			if (flag) {
				dataMap.put("resultCode", "200");
				dataMap.put("reason", "登陆成功！");
				request.getSession().setAttribute("username", usernameBySession);
			} else {
				dataMap.put("resultCode", "400");
				dataMap.put("reason", "密码不正确！");
			}
		}else{
			dataMap.put("resultCode", "600");
			dataMap.put("reason", "用户信息已过期！");
		}
		System.out.println(JSONObject.fromObject(dataMap).toString());
		FileUtil.write4ajax(JSONObject.fromObject(dataMap).toString(), response);
	}

}
