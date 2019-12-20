# springboot集成shiro
## 简介：
Apache Shiro是一个强大且易用的Java安全框架，执行身份认证、授权、加密和会话管理。使用Shiro的易于理解的API，可以快速、轻松地获得任何应用程序，从最小的移动应用程序到最大的网络和企业应用程序。
## 架构：
        Authentication：身份认证/登录，验证用户是不是拥有相应的身份；
        Authorization：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；
        Session Manager：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通JavaSE环境的，也可以是如Web环境的；
        Cryptography：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储；
        Web Support：Web支持，可以非常容易的集成到Web环境；
        Caching：缓存，比如用户登录后，其用户信息、拥有的角色/权限不必每次去查，这样可以提高效率；
        Concurrency：shiro支持多线程应用的并发验证，即如在一个线程中开启另一个线程，能把权限自动传播过去；
        Testing：提供测试支持；
        Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问；
        Remember Me：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了。
## 主要组件：
### Subject：
        主体，代表了当前“用户”，所有Subject都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager，可以把Subject认为是一个门面，SecurityManager才是实际的执行者；
### SecurityManager：
        安全管理器，所有与安全有关的操作都会与SecurityManager交互，且它管理着所有Subject，负责与后边介绍的其他组件进行交互（类似于SpringMVC中的DispatcherServlet控制器）；
### Realm：
        域，Shiro从从Realm获取安全数据（如用户、角色、权限），就是说SecurityManager要验证用户身份，那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法，也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作，可以把Realm看成DataSource，即安全数据源。
## 认证的流程：
        创建SecurityManager安全管理器 >主体Subject提交认证信息 >SecurityManager安全管理器认证 >SecurityManager调用Authenticator认证器认证 >Realm验证
