package com.hxd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色对应实体类
 * Created by hee on 2019/12/19 13:51
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Role {

    private String id;

    private String roleName;

}
