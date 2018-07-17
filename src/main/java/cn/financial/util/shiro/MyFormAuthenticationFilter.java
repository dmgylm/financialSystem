package cn.financial.util.shiro;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import cn.financial.util.ElementConfig;
import cn.financial.util.ElementXMLUtils;
/**
 * 自定义过滤器实现login跳转指定路径
 * @author gs
 * 2018/6/13
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter{  
    /**
     * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("url*********"+req.getRequestURL());
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        // 设置响应编码
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.sendError(401,"SESSION过期,请重新登录!");
        // 获取响应流
//        PrintWriter out = response.getWriter();
        // 组装要返回的数据
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.putAll(ElementXMLUtils.returnValue(ElementConfig.USER_SESSION_OVERDUE));
        // 写数据
//        System.out.println(map.toString()); //这个地方没有乱码的话，说明你的控制台（IDE） - xml文件的编码是一致的，如果浏览器出现乱码
        // 有两种可能，第一response响应的编码不对，第二是浏览器编码和响应的编码不一致 稍等
//        out.write(map.toString());
//        // 刷新流
//        out.flush();
//        // 关闭流
//         out.close();
        // 如果是未登录的Ajax请求，则拦截
        return true;
    }
}
