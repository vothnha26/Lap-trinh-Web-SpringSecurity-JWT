// File: vn.iotstar.controller.AppController.java
package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.iotstar.service.ProductService; // Cần dùng để lấy danh sách sản phẩm

@Controller
public class AppController {

    @Autowired
    private ProductService productService;

    /**
     * Đường dẫn / hoặc /index (Trang chủ)
     * Quyền truy cập: CÔNG KHAI (permitAll())
     * Trả về file: index.html
     */
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        // Lấy danh sách sản phẩm để hiển thị trên trang chủ
        model.addAttribute("products", productService.findAll());
        return "index"; 
    }
    
    /**
     * Đường dẫn /login (Trang đăng nhập tùy chỉnh)
     * Quyền truy cập: CÔNG KHAI (permitAll())
     * Trả về file: login.html
     */
    @GetMapping("/login")
    public String login() {
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