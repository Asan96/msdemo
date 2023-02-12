package com.ydjk.msdemo.shiro;

import com.ydjk.msdemo.service.UserService;
import com.ydjk.msdemo.utils.JWTUtil;
import com.ydjk.msdemo.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    //根据token判断此Authenticator是否使用该realm
    //必须重写不然shiro会报错
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如@RequiresRoles,@RequiresPermissions之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token=principals.toString();
        Long userId = JWTUtil.getUserId(token);
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        String key = String.format("%s:uid%s", RedisUtil.UserDbName,userId);
        Object cache = redisUtil.get(key);
        if (cache instanceof Map){
            Map<String, Object> cacheMap = (Map<String, Object>) cache;
            info.addRole((boolean) cacheMap.get("is_teacher") ? "teacher" : "student");
        }
        //查询数据库来获取用户的权限
//        info.addStringPermissions(user.getPermissionList().stream().map(Permissions::getName).collect(Collectors.toList()));
        return info;
    }


    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可，在需要用户认证和鉴权的时候才会调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt= (String) token.getCredentials();
        String username;
        //decode时候出错，可能是token的长度和规定好的不一样了
        try {
            username= JWTUtil.getUsername(jwt);
        }catch (Exception e){
            System.out.println("token非法，不是规范的token，可能被篡改了，或者过期了");
            throw new AuthenticationException("token非法，不是规范的token，可能被篡改或过期");
        }
        if (!JWTUtil.verify(jwt)||username==null){
            System.out.println("token认证失效，token错误或者过期，重新登陆");
            throw new AuthenticationException("认证失效，登录错误或者过期，请重新登陆");
        }
        Long userId = JWTUtil.getUserId(jwt);
//        String cacheKey = userService.getCacheKey(userId);
//        HashMap<String, Object> cache = (HashMap<String, Object>) redisUtil.get(cacheKey);
//        if (cache == null || !cache.get("token").equals(jwt)){
//            log.error("token 不一致 -----------------");
//            throw new AuthenticationException("登录已过期，请重新登录");
//        }
        return new SimpleAuthenticationInfo(jwt,jwt,"ShiroRealm");
    }
}

