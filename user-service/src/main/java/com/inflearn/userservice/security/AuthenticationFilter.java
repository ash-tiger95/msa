package com.inflearn.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inflearn.userservice.dto.UserDto;
import com.inflearn.userservice.service.UserService;
import com.inflearn.userservice.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter { // 인증

    private UserService userService;
    private Environment env;

    /*
    AuthenticationFilter는 WebSecurity configure(HttpSecurity http)에서 getAuthenticationFilter()를 호출하면서 AuthenticationFilter 인스턴스를 생성한다.
    인스턴스를 생성하면 AuthenticationFilter 클래스가 빈에 등록이 된다.
     */
    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try{
            // 1. 유저정보 받기, 그 모양은 InputStream (POST로 전달되는 건 파라미터로 받을 수 없어서 InputStream으로 받는다.)
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // 3. 인증작업 요청 (Email, Password 검사)
            return getAuthenticationManager().authenticate(
                // 2. 인증정보로 만들기, UsernamePasswordAuthenticationToken으로 변경시켜줘야 한다.
                // 사용자 Email, Password를 Spring Security에서 사용할 수 있는 형태로 변경해줘야한다. 그 모양은 UsernamePasswordAuthenticationToken
                new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()) // (Email, Password, 권한)
            );
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    /*
    로그인을 성공했을 때, 어떤 처리를 할건지(토큰 만들거나, 만료시간, 어떤 반환값을 줄건지 등등)
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
       String email = ((User)authResult.getPrincipal()).getUsername(); // Spring Security getUsername()는 우리가 입력하는 Email
       UserDto userDetails = userService.getUserDetailsByEmail(email);

       // JWT 만들기
        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time")))) // 현재시간 + 하루 = 내일 이 시간까지
                .signWith(SignatureAlgorithm.HS256, env.getProperty("token.secret")) // 암호화
                .compact();

        response.addHeader("token",token);
        response.addHeader("userId",userDetails.getUserId());
    }
}
