package cn.financial.util.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import cn.financial.model.User;
import cn.financial.service.UserService;
import cn.financial.service.impl.UserServiceImpl;

public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserServiceImpl userService;

    
	/**
	 * 授权
	 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.findPermissions(username));

        return authorizationInfo;
    }
    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

    	UsernamePasswordToken upToken = (UsernamePasswordToken)token;
    	String username = upToken.getUsername();
      

        User user = userService.getUserByName(username);
        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }

     /*   if(Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }*/

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getName(), //用户名
                user.getPwd(), //密码
                ByteSource.Util.bytes(user.getSalt()),//uuid生成
                getName()  //realm name
        );
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }
    
	public UserService getUserService() {
		return userService;
	}

    
	
}
