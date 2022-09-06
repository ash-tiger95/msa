package com.inflearn.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration // 다른 빈들보다 우선순위가 앞서 등록을 한다.
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 권한 관련 설정
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users/**").permitAll(); // prefix가 붙은 URI에 대해서는 인증작업이 필요없음을 의미

        http.headers().frameOptions().disable(); // h2 DB 웹 페이지를 위함
    }

}
