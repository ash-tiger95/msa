package com.inflearn.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor // arg 다 갖이고 있는 생성자 만들기
@NoArgsConstructor // arg 없는 디폴트 생성자 만들기
public class Greeting {
    @Value("${greeting.message}") // yml args에 있는 값 불러오기
    private String message;
}
