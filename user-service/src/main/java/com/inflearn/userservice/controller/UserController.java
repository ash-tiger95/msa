package com.inflearn.userservice.controller;

import com.inflearn.userservice.vo.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    private Environment env;

    @Autowired
    private Greeting greeting;

    @Autowired
    public UserController(Environment env) {
        this.env = env;
    }

    @GetMapping("/health_check")
    public String status(){
        return "Working User Service";
    }

    /*
    * .yml args 불러오는 방법
    * 방법1) env.getProperty
    * 방법2) @Value(${""})
    * */
    @GetMapping("/welcome")
    public String welcome(){
//        return env.getProperty("greeting.message"); // yml args에 있는 값 불러오기
        return greeting.getMessage();
    }
}
