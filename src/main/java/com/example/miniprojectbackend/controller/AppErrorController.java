package com.example.miniprojectbackend.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// ErrorController는 SpringBoot에서 404 에러를 처리하는 데 사용됨
@Controller
public class AppErrorController implements ErrorController {
    @GetMapping("/error")
    public String handleError(){
        return "/";
    }
}
