package com.ljb.springbootseta.config;

import com.ljb.springbootseta.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    //继承WebSecurityConfigurerAdapter，之后会进行配置



//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  //这里使用SpringSecurity提供的BCryptPasswordEncoder
//        auth
//                .inMemoryAuthentication() //直接验证方式，之后会讲解使用数据库验证
//                .passwordEncoder(encoder) //密码加密器
//                .withUser("test")   //用户名
//                .password(encoder.encode("123456"))   //这里需要填写加密后的密码
//                .roles("user");   //用户的角色（之后讲解）
//    }          //使用内存用户

     @Autowired
     UserAuthService service;        //数据库支持

    @Resource
    PersistentTokenRepository repository;      //数据源准备(记住我)

    @Bean
    public PersistentTokenRepository jdbcRepository(@Autowired DataSource dataSource){   //jdbc
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();  //使用基于JDBC的实现
        repository.setDataSource(dataSource);   //配置数据源
//        repository.setCreateTableOnStartup(true);   //启动时自动创建用于存储Token的表（建议第一次启动之后删除该行）
        return repository;
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(service)   //使用自定义的Service实现类进行验证
                .passwordEncoder(new BCryptPasswordEncoder());   //依然使用BCryptPasswordEncoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   //关闭csrf

                .authorizeRequests()   //首先需要配置哪些请求会被拦截，哪些请求必须具有什么角色才能访问
                .antMatchers("/static/**").permitAll()    //静态资源，使用permitAll来运行任何人访问（注意一定要放在前面）
                .antMatchers("/head/ds").hasRole("user")
                .antMatchers("/**").hasRole("user")   //所有请求必须登陆并且是user角色才可以访问（不包含上面的静态资源）
                .and()

                .formLogin()       //配置Form表单登陆
                .loginPage("/head/welcome")       //登陆页面地址（GET）
                .loginProcessingUrl("/book_manager/login")      //form表单提交地址（POST）
                .defaultSuccessUrl("/head/homepage",true)    //登陆成功后跳转的页面（GET），可以通过Handler实现高度自定义
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//                        System.out.print("成功登录");
//                    }
//                })
                  //登陆页面也需要允许所有人访问
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")    //退出登陆的请求地址
                .logoutSuccessUrl("/head/welcome")   //退出后重定向的地址

                .and()
                .rememberMe()   //开启记住我功能
                .rememberMeParameter("remember")  //登陆请求表单中需要携带的参数，如果携带，那么本次登陆会被记住
//                .tokenRepository(new InMemoryTokenRepositoryImpl())  //这里使用的是直接在内存中保存的TokenRepository实现
        //TokenRepository有很多种实现，InMemoryTokenRepositoryImpl直接基于Map实现的，缺点就是占内存、服务器重启后记住我功能将失效
        //后面我们还会讲解如何使用数据库来持久化保存Token信息

                .tokenRepository(repository)
                .tokenValiditySeconds(60 * 60 * 24 * 7);  //Token的有效时间（秒）默认为14天

    }


    @PostConstruct        //在一开始就执行
    public void init(){
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);//子线程可获取context
        //默认只能主线程获取
    }

}
