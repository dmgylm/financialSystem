package cn.financial.util.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
/**
 * 自定义过滤器实现login跳转指定路径
 * @author gs
 * 2018/6/13
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter{
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, Se)
}
