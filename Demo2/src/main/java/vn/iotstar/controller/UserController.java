// File: vn.iotstar.controller.UserController.java
package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import vn.iotstar.entity.UserInfo;
import vn.iotstar.repository.UserInfoRepository;

import java.util.List;

@RestController // Quan trọng: Đảm bảo class này là REST Controller để xử lý JSON/Data
@RequestMapping("/api") // Nên thêm tiền tố /api để phân biệt rõ API và View
public class UserController {

    @Autowired
    private UserInfoRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // API để thêm user mới (Dùng để khởi tạo dữ liệu ban đầu qua Postman)
    @PostMapping("/users/new")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully: " + userInfo.getUsername();
    }

    // API yêu cầu quyền ADMIN (kiểm tra phân quyền REST)
    @GetMapping("/users/all")
    public List<UserInfo> getAllUsers() {
        return repository.findAll();
    }
}