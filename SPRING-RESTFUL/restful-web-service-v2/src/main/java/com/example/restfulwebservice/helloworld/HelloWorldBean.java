package com.example.restfulwebservice.helloworld;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data   // getter, setter 자동 생성
@AllArgsConstructor // 모든 변수를 포함하는 생성자 자동 생성
public class HelloWorldBean {
    private String message;
}
