package com.hxd.dao;


import com.hxd.entity.Permissions;
import com.hxd.entity.Role;
import com.hxd.entity.User;
import java.util.List;

public interface LoginDao {

    User queryUserByName(String name);

    List<Role> queryRoleListByUserId(String id);

    List<Permissions> queryPermissionsListByRoleId(String id);
}
