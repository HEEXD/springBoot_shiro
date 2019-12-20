package com.hxd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hee on 2019/12/19 13:46
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = -2326828042703779424L;

    private String id;

    private String username;

    private String age;

    private String password;

}
