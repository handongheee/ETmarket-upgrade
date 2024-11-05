package kr.co.sist.etmarket.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserTempController {

    @GetMapping("/check-login-status")
    public Map<String,Object> checkLoginStatus(HttpSession session) {

        Long uid = (Long) session.getAttribute("myUserId");
        String isLogin = (String) session.getAttribute("loginok");

        Map<String, Object> response = new HashMap<>();

        if (uid != null && isLogin.equals("yes")) {
            response.put("isLoggedin", true);
            response.put("userId", uid);
        } else {
            response.put("isLoggedin", false);
        }
        return response;
    }

}
