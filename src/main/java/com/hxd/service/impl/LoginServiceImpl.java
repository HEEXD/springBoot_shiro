package com.hxd.service.impl;

import com.hxd.dao.LoginDao;
import com.hxd.entity.Permissions;
import com.hxd.entity.Role;
import com.hxd.entity.User;
import com.hxd.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by hee on 2019/12/19 14:24
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private LoginDao loginDao;

    /**
     * 查询用户信息
     */
    @Override
    public Map<String, Object> getUserByName(String name) {
        // 查询用户
        User user = this.loginDao.queryUserByName(name);
        if (user == null) {
            return null;
        }

        // 查询用户拥有的角色
        List<Role> roleList = this.loginDao.queryRoleListByUserId(user.getId());

        // 查询此用户拥有的权限
        List<Permissions> permissionsList = new ArrayList<>();
        for (Role role : roleList) {
            List<Permissions> list = this.loginDao.queryPermissionsListByRoleId(role.getId());
            permissionsList.addAll(list);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roleList", roleList);
        map.put("permissionsList", permissionsList);

        return map;
    }
}
