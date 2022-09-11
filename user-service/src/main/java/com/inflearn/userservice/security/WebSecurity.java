package com.inflearn.userservice.security;

import com.inflearn.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

@Configuration // 다른 빈들보다 우선순위가 앞서 등록을 한다.
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env){
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 권한 관련 설정 configure
        http.csrf().disable();
        // http.authorizeRequests().antMatchers("/users/**").permitAll(); // prefix가 붙은 URI에 대해서는 인증작업이 필요없음을 의미

        // 4. 인증작업
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        http.authorizeRequests().antMatchers("/users/**")
                .hasIpAddress("172.30.1.19") // IP검사
                .and()
                .addFilter(getAuthenticationFilter()); // 이 필터를 통과를 한 데이터에 한해서 권한을 부여하고 작업 진행하겠다는 의미 (우리가 만든 AuthenficationFilter())

        http.headers().frameOptions().disable(); // h2 DB 웹 페이지를 위함
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{ // 인증처리
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env); // 인스턴스 생성하면서 빈 전달

        /*
            AuthenticationFilter authenticationFilter = new AuthenticationFilter();일 때는 아래 코드가 필요햤는데,
            AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env);에서
                authenticationManager()를 호출하면서 아래코드가 필요없어졌다.
         */

        // authenticationFilter.setAuthenticationManager(authenticationManager()); // 인증처리를 하기 위해서 Spring Security에서 가져온 메니저로 인증처리

        return authenticationFilter;
    }

    // db_pwd(encrypted) == input_pwd(encrypted_pwd), 디비 패스워드가 암호화되어 있으니, 들어오는 패스워드도 암호화해서 비교를 해야한다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 인증 관련 설정 configure, 인증이 되야 권한부여가 가능하다
        auth.userDetailsService(userService) // 사용자가 전달한 유저네임, 패스워드로 로그인 처리를 해준다. (select pwd from users where email = ? 역할)
                .passwordEncoder(bCryptPasswordEncoder); // Spring Security에서 제공해주는 함수, pwd 암호화 처리
    }
}
