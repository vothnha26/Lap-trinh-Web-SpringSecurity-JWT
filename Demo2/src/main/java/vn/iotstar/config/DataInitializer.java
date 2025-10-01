package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import vn.iotstar.entity.UserInfo;
import vn.iotstar.repository.UserInfoRepository;

@Component
public class DataInitializer {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Phương thức sẽ chạy ngay sau khi Spring Boot khởi động thành công
    @EventListener
    public void init(ApplicationReadyEvent event) {
        
        // Kiểm tra nếu chưa có User nào trong DB thì mới khởi tạo
        if (repository.count() == 0) {
            System.out.println("--- Bắt đầu khởi tạo dữ liệu người dùng tự động ---");
            
            // 1. Tạo User ADMIN (admin/123)
            UserInfo admin = new UserInfo();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123")); // MÃ HÓA TẠI ĐÂY
            admin.setEmail("admin@iotstar.vn");
            admin.setRoles("ROLE_ADMIN,ROLE_USER"); 
            repository.save(admin);

            // 2. Tạo User THƯỜNG (user/123)
            UserInfo user = new UserInfo();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("123")); // MÃ HÓA TẠI ĐÂY
            user.setEmail("user@iotstar.vn");
            user.setRoles("ROLE_USER");
            repository.save(user);

            System.out.println("--- Khởi tạo dữ liệu thành công. Users: admin/123, user/123 ---");
        } else {
            System.out.println("--- CSDL đã có dữ liệu. ---");
        }
    }
}