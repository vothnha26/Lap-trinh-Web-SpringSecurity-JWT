package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    /**
     * Đường dẫn /home (Trang chủ)
     * Quyền truy cập: CÔNG KHAI (permitAll())
     * Trả về file: home.html
     */
    @GetMapping({"/", "/home"})
    public String home() {
        return "home"; 
    }
    
    /**
     * Đường dẫn /users/profile (Trang hồ sơ)
     * Quyền truy cập: CẦN XÁC THỰC (authenticated())
     * Trả về file: profile.html
     */
    @GetMapping("/users/profile")
    public String userProfile() {
        return "profile"; 
    }
    
    /**
     * Đường dẫn /login (Trang đăng nhập tùy chỉnh)
     * Quyền truy cập: CÔNG KHAI (permitAll())
     * Trả về file: login.html
     */
    @GetMapping("/login")
    public String login() {
        // Spring Security sẽ tự động xử lý POST /login
        return "login"; 
    }

    /**
     * Đường dẫn /403 (Trang lỗi cấm truy cập)
     * Quyền truy cập: CÔNG KHAI (permitAll())
     * Trả về file: 403.html
     */
    @GetMapping("/403")
    public String accessDenied() {
        return "403"; 
    }
}