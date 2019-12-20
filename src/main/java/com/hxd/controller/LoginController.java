package com.hxd.controller;

import com.hxd.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by hee on 2019/12/19 16:14
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(User user, Model model, String rememberMe) {
        // 添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getUsername(), user.getPassword()
        );
        // 记住登录状态
        if (rememberMe != null) {
            usernamePasswordToken.setRememberMe(true);
        }
        try {
            // 进行验证
            subject.login(usernamePasswordToken);
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码不正确");
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "账号不存在");
        } catch (AuthenticationException e) {
            model.addAttribute("msg", "状态不正常");
        }
        if (subject.isAuthenticated()) {
            model.addAttribute("username", user.getUsername());
            return "success";
        } else {
            usernamePasswordToken.clear();
            return "login";
        }
    }

    @RequiresRoles("admin")// 角色
    @RequiresPermissions("addUser")// 权限
    @RequestMapping("/addUser")
    @ResponseBody
    public String addUser() {
        return "addUser";
    }

    @RequiresRoles("admin")
    @RequiresPermissions("queryUser")
    @RequestMapping("/queryUser")
    @ResponseBody
    public String queryUser() {
        return "queryUser";
    }

    @RequiresRoles("admin")
    @RequiresPermissions("updateUser")
    @RequestMapping("/updateUser")
    @ResponseBody
    public String updateUser() {
        return "updateUser";
    }

    @RequiresRoles("manage")
    @RequiresPermissions("delUser")
    @RequestMapping("/delUser")
    @ResponseBody
    public String delUser() {
        return "delUser";
    }
}
