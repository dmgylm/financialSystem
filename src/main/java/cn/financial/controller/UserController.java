package cn.financial.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.financial.model.User;
import cn.financial.service.UserService;
import cn.financial.service.impl.UserServiceImpl;


@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @RequestMapping(value="/manage/index")
    public void getUser(HttpServletRequest request,HttpServletResponse respons){
	Integer userId=1;
   	User u=userService.selectUserById(userId);
   	System.out.println(u.getName());
       }
  
   
}
