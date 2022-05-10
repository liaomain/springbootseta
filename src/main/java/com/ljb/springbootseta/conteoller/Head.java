package com.ljb.springbootseta.conteoller;

import com.ljb.springbootseta.service.Headse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j   //小辣椒的快速使用日志
@RequestMapping("head")  //地址
@Controller  //mvc
@RestController
public class Head {

    @Autowired
    Headse s;

    @RequestMapping(value = "welcome" ,method = RequestMethod.GET)//或者post
    public ModelAndView hy() {
        ModelAndView mo=new ModelAndView();
//        mo.addObject("xiaoxi","欢迎");
//        s.test();

        System.out.println("+++++++++++++++++++++++++++++++");
        mo.setViewName("welcome");
        return mo;
    }


    @RequestMapping(value = "homepage" ,method = RequestMethod.GET)//或者post
    public ModelAndView zy() {
        ModelAndView mo=new ModelAndView();
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        //日志级别从低到高分为TRACE < DEBUG < INFO < WARN < ERROR < FATAL，SpringBoot默认只会打印INFO以上级别的信息。
        log.info("用户访问了一次登陆界面");
        mo.setViewName("homepage");
        return mo;
    }

    @RequestMapping(value = "logout" ,method = RequestMethod.GET)//或者post
    public ModelAndView lo() {
        ModelAndView mo=new ModelAndView();
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        mo.setViewName("welcome");
        return mo;
    }

//    @RequestMapping(value = "homepage" ,method = RequestMethod.GET)//或者post
//    public String zy() {
//
//        return "homepage";
//    }

    @RequestMapping(value = "ds" ,method = RequestMethod.GET)//或者post
    public String duihua() {


        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println(user.getUsername());        //获取name
        System.out.println(user.getAuthorities());    //  获取 权限加入了ROLE 如：ROLE_user
        return "ds";
    }












}
