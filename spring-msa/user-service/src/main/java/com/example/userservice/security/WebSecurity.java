package com.example.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    // 권한 작업 -> 인증에 따라 무엇을 할 수 있는지
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users/**").permitAll();  // "/users/**" URI 허용

        // 프레임 상으로 구별되어 있는 HTTP 페이지에서 정상적으로 동작
        // 추가하지 않으면 h2-console 접근 안됨
        http.headers().frameOptions().disable();
    }

}
