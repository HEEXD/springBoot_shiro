package com.hxd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 权限对应实体类
 * Created by hee on 2019/12/19 13:53
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Permissions {

    private String id;

    private String permissionsName;

}
