package com.hxd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * shiro认证的流程：
 * 创建SecurityManager安全管理器 >
 *      主体Subject提交认证信息 >
 *              SecurityManager安全管理器认证 >
 *                      SecurityManager调用Authenticator认证器认证 >
 *                              Realm验证
 * 有几个概念：
 * Subject：主体，代表了当前“用户”；所有Subject都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager；
 *          可以把Subject认为是一个门面；SecurityManager才是实际的执行者；
 * SecurityManager安全管理器：所有与安全有关的操作都会与SecurityManager交互；且它管理着所有Subject；
 *          负责与后边介绍的其他组件进行交互。（类似于SpringMVC中的DispatcherServlet控制器）
 * Realm：域，Shiro从从Realm获取安全数据（如用户、角色、权限），就是说SecurityManager要验证用户身份，
 *          那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法；也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作；
 *          可以把Realm看成DataSource，即安全数据源。
 *
 */
@SpringBootApplication
@MapperScan("com.hxd.dao")
public class ApplicationRun {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRun.class, args);
    }

}
