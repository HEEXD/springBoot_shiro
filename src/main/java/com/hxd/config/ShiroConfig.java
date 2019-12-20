package com.hxd.config;

import com.hxd.shiro.CustomRealm;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 把CustomRealm和SecurityManager等加入到spring容器
 * Created by hee on 2019/12/19 15:54
 */
@Configuration
public class ShiroConfig {

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);// 以cglib动态代理方式生成代理类，默认为false，也就是默认使用JDK动态代理
        return defaultAAP;
    }

    /**
     * 开启shiro aop注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 将自己的验证方式加入容器
     */
    @Bean
    public CustomRealm myCustomRealm() {
        return new CustomRealm();
    }

    /**
     * 权限管理，配置主要是Realm的管理认证
     * 注意：SecurityManager导包的时候选org.apache.shiro.mgt.SecurityManager;而不是java.lang.SecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myCustomRealm());//设置realm
        securityManager.setRememberMeManager(cookieRememberMeManager());// 注入cookie管理器
        return securityManager;
    }

    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();

        map.put("/login", "anon");// anon:所有url都都可以匿名访问,先配置anon再配置authc
        //map.put("/**", "authc");// authc拦截器会判断用户是否是通过Subject.login(isAuthenticated()==true)登录的，如果是才放行
        map.put("/**", "user");// user拦截器只要用户登录(isRemembered()==true or isAuthenticated()==true)过即可访问成功
        map.put("/logout", "logout");// 登出
        filterFactoryBean.setLoginUrl("/login");// 登录
        //filterFactoryBean.setSuccessUrl("/skipIndex");// 登录成功时跳转
        filterFactoryBean.setUnauthorizedUrl("/error");// 错误页面，认证不通过时跳转
        filterFactoryBean.setFilterChainDefinitionMap(map);
        return filterFactoryBean;
    }

    /**
     * 处理未授权的异常，返回自定义的错误页面（403）
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("UnauthorizedException", "403.html");
        resolver.setExceptionMappings(properties);
        return resolver;
    }

    /**
     * cookie管理对象
     * 注意：rememberMeManager继承了AbstractRememberMeManager，
     * 然而AbstractRememberMeManager的构造方法中每次都会重新生成对称加密密钥,
     * 意味着每次重启程序都会重新生成一对加解密密钥，所以需要手动设置一个加解密密钥
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        // 这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // cookie的有效时间为30天，单位秒
        simpleCookie.setMaxAge(2592000);
        cookieRememberMeManager.setCookie(simpleCookie);
        // 手动设置一个加解密密钥
        cookieRememberMeManager.setCipherKey(Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
        return cookieRememberMeManager;
    }

}
