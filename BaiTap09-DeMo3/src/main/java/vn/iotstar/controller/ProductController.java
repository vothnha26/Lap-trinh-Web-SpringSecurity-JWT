// File: vn.iotstar.controller.ProductController.java
package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.entity.Product;
import vn.iotstar.service.ProductService;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // --- 1. Thêm sản phẩm (GET: Mở Form) ---
    // URL: /products/new
    // Cần ROLE_ADMIN (Đã cấu hình trong SecurityConfig)
    @GetMapping("/new")
    public String newProductForm(Model model) {
        // Gửi một đối tượng Product rỗng để form có thể liên kết (binding)
        model.addAttribute("product", new Product());
        return "new_product"; 
    }
    
    // --- 2. Lưu sản phẩm (POST: Xử lý Form) ---
    // URL: /products/save
    // Cần ROLE_ADMIN
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              RedirectAttributes ra) {
        
        productService.save(product);
        ra.addFlashAttribute("message", "Lưu sản phẩm thành công!");
        return "redirect:/"; // Quay về trang chủ
    }

    // --- 3. Sửa sản phẩm (GET: Mở Form) ---
    // URL: /products/edit/{id}
    // Cần ROLE_ADMIN
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Optional<Product> optionalProduct = productService.findById(id);

        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            return "edit_product"; 
        } else {
            // Nếu không tìm thấy sản phẩm, chuyển về trang chủ
            return "redirect:/"; 
        }
    }

    // --- 4. Xóa sản phẩm (GET/POST: Xử lý Xóa) ---
    // URL: /products/delete/{id}
    // Cần ROLE_ADMIN
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            productService.deleteById(id);
            ra.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa sản phẩm này.");
        }
        return "redirect:/";
    }
}