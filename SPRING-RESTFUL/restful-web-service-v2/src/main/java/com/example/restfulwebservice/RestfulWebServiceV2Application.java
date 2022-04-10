package com.example.restfulwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class RestfulWebServiceV2Application {

    public static void main(String[] args) {
        SpringApplication.run(RestfulWebServiceV2Application.class, args);
    }

    @Bean   // Spring Boot가 초기화될 때 해당 내용이 메모리에 저장됨
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        return localeResolver;
    }

}
