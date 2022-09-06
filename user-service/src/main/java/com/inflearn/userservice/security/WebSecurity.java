package com.inflearn.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.Filter;

@Configuration // 다른 빈들보다 우선순위가 앞서 등록을 한다.
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 권한 관련 설정
        http.csrf().disable();
        // http.authorizeRequests().antMatchers("/users/**").permitAll(); // prefix가 붙은 URI에 대해서는 인증작업이 필요없음을 의미

        // 4. 인증작업
        http.authorizeRequests().antMatchers("/users/**")
                .hasIpAddress("") // IP검사
                .and()
                .addFilter(getAuthenticationFilter()); // 이 필터를 통과를 한 데이터에 한해서 권한을 부여하고 작업 진행하겠다는 의미

        http.headers().frameOptions().disable(); // h2 DB 웹 페이지를 위함
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{ // 인증처리
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager()); // 인증처리를 하기 위해서 Spring Security에서 가져온 메니저로 인증처리

        return authenticationFilter;
    }

}
