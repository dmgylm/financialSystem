package cn.financial.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 控制页面跳转
 * @author rzm
 *
 */
@Controller
public class MainController {
    //login
    @RequestMapping(value="/manage/login")
    public ModelAndView login(HttpServletRequest request,HttpServletResponse respons){
    ModelAndView andView=new ModelAndView();
    andView.setViewName("manage/login");
    return andView;
    }
    //index
	@RequestMapping(value="/manage/index")
    public ModelAndView index(HttpServletRequest request,HttpServletResponse respons){
	ModelAndView andView=new ModelAndView();
	andView.setViewName("manage/index");
	return andView;
    }
}
