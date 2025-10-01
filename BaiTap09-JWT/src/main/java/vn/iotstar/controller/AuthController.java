package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {

    @GetMapping
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("login")
    public String index() {
        return "login";
    }

    @GetMapping("user/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("logout")
    public String logout() {
        return "redirect:/login";
    }
}
