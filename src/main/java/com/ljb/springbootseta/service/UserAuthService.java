package com.ljb.springbootseta.service;

import com.ljb.springbootseta.entity.Ue;
import com.ljb.springbootseta.mapper.Daoa;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class UserAuthService implements UserDetailsService {

    @Resource
    Daoa mapper;


    //使用数据库对比的Service服务
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Ue u = mapper.denglu(s);  //从数据库根据用户名获取密码
        if(u.getPassword() == null)
            throw new UsernameNotFoundException("登录失败，用户名或密码错误！");
        return User   //这里需要返回UserDetails，SpringSecurity会根据给定的信息进行比对
                .withUsername(s)
                .password(u.getPassword())   //直接从数据库取的密码
                .roles(u.getRole())   //用户角色
                .build();
    }

}
