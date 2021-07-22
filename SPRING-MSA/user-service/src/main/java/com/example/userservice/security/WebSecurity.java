package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    // 권한 작업 -> 인증에 따라 무엇을 할 수 있는지
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // http.authorizeRequests().antMatchers("/users/**").permitAll();  // "/users/**" URI 허용

        // Actuator 정보는 권한 없이 사용할 수 있도록 설정
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();


        /* 도커에 올리니까 잘 작동하지 않음... 어떻게 고치지??? */
        http.authorizeRequests().antMatchers("/**")    // 모든 작업을 우선 차단
                .permitAll()
//                .hasIpAddress("172.30.1.16")    // 허용할 IP
                .and()
                .addFilter(getAuthenticationFilter());


        // 프레임 상으로 구별되어 있는 HTTP 페이지에서 정상적으로 동작
        // 추가하지 않으면 h2-console 접근 안됨
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager(), userService, env);

        return authenticationFilter;
    }

    // SELECT PWD FROM USERS WHERE email = ?
    // dw_pwd(encrypted) == input_pwd(encrypted)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

}
