package kr.co.sist.etmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String start(){
        return "main/main";
    }

    @GetMapping("/list")
    public String list(){
        return "main/list";
    }
}