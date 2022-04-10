package com.example.restfulwebservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 학습환경에서 h2-console 및 여러 호출을 Spring Security가 차단하지 않게 하기 위함
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("test")
                    .password("{noop}test") // 인코딩 없이 사용할 수 있도록 {noop} 추가
                    .roles("USER"); // 로그인 후 USER의 role을 부여받는다.
        }

}
