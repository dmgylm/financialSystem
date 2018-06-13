package cn.financial.util.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
/**
 * 自定义过滤器实现login跳转指定路径
 * @author gs
 * 2018/6/13
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter{
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,ServletRequest request, ServletResponse response) throws Exception {
        //issueSuccessRedirect(request, response);
        //we handled the success redirect directly, prevent the chain from continuing:
        WebUtils.getAndClearSavedRequest(request);//会先清理原先的地址
        WebUtils.redirectToSavedRequest(request, response, "/index");
        return false;
    }
}
