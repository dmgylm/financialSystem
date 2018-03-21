package cn.financial.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import cn.financial.service.impl.UserServiceImpl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 * @author gs
 *
 */
public class SysUserFilter extends PathMatchingFilter {

    @Autowired
    private UserServiceImpl userService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String username = (String)SecurityUtils.getSubject().getPrincipal();
        request.setAttribute("user", userService.getUserByName(username));
        System.out.println("****************"+username);
        return true;
    }
}
