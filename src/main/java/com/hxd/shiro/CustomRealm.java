package com.hxd.shiro;

import com.hxd.entity.Permissions;
import com.hxd.entity.Role;
import com.hxd.entity.User;
import com.hxd.service.LoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 自定义Realm用于查询用户的角色和权限信息并保存到权限管理器
 * Created by hee on 2019/12/19 15:33
 */
public class CustomRealm extends AuthorizingRealm {

    @Resource
    private LoginService loginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登录用户名
        String name = (String) principalCollection.getPrimaryPrincipal();
        // 根据用户名去数据库查询用户信息
        Map<String, Object> user = loginService.getUserByName(name);
        // 添加角色
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<Role> roleList = (List<Role>) user.get("roleList");
        for (Role role : roleList) {
            simpleAuthorizationInfo.addRole(role.getRoleName());
        }
        // 添加权限
        List<Permissions> permissionsList = (List<Permissions>) user.get("permissionsList");
        for (Permissions permissions : permissionsList) {
            simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName());
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 加这一步的目的是在Post请求的时候会先进认证，然后再到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        // 获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        // 根据用户名去数据库查询用户信息
        Map<String, Object> userMap = loginService.getUserByName(name);
        if (userMap == null) {
            return null;
        } else {
            User user = (User) userMap.get("user");
            // 这里验证authenticationToken和simpleAuthenticationInfo的信息
            return new SimpleAuthenticationInfo(name, user.getPassword(), getName());
        }
    }
}
