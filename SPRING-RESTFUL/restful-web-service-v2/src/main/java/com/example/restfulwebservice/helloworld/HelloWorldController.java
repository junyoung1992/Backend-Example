package com.example.restfulwebservice.helloworld;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController // Controller + ResponseBody
@RequiredArgsConstructor
public class HelloWorldController {

    private final MessageSource messageSource;

    // GET
    // /hello-world (endpoint)
    // @RequestMapping(method=RequestMethod.GET, path="/hello-world)
    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World");
    }

    // hello-world-bean/path-variable/kenneth
    @GetMapping("/hello-world-bean/path-variable/{name}")
    // @PathVariable(value="name") String name
    // @PathVariable 대상의 이름이 같으면 value 설정이 생략 가능
    public HelloWorldBean helloWorldBean(@PathVariable(value="name") String name) {
        return new HelloWorldBean(String.format("Hello World, %s", name));
    }

    @GetMapping("/hello-world-internationalized")
    public String helloWorldInternationalized(
            @RequestHeader(name="Accept-Language", required = false) Locale locale) {
        return messageSource.getMessage("greeting.message", null, locale);
    }


}
