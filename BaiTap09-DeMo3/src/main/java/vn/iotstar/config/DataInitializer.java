package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import vn.iotstar.entity.Role;
import vn.iotstar.entity.Users;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Phương thức này tự động chạy sau khi Spring Boot đã sẵn sàng.
     */
    @EventListener
    public void init(ApplicationReadyEvent event) {
        
        // Chỉ khởi tạo Users nếu chưa có User nào tồn tại
        if (userRepository.count() == 0) {
            System.out.println("--- Bắt đầu khởi tạo dữ liệu Users (Liên kết với Roles đã có) ---");
            
            // 1. TÌM KIẾM CÁC ROLE ĐÃ TẠO BẰNG SQL
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
            
            if (!optionalRoleAdmin.isPresent() || !optionalRoleUser.isPresent()) {
                 System.err.println("LỖI: Không tìm thấy ROLE_ADMIN hoặc ROLE_USER. Vui lòng kiểm tra lại CSDL!");
                 return;
            }
            
            Role roleAdmin = optionalRoleAdmin.get();
            Role roleUser = optionalRoleUser.get();

            String encodedPassword = passwordEncoder.encode("123");

            // 2. Tạo User ADMIN và thiết lập Roles
            Users admin = new Users();
            admin.setUsername("admin");
            admin.setPassword(encodedPassword);
            admin.setEmail("admin@example.com");
            
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleAdmin);
            adminRoles.add(roleUser);
            admin.setRoles(adminRoles);
            
            userRepository.save(admin);

            // 3. Tạo User THƯỜNG và thiết lập Roles
            Users user = new Users();
            user.setUsername("user");
            user.setPassword(encodedPassword);
            user.setEmail("user@example.com");
            
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleUser);
            user.setRoles(userRoles);
            
            userRepository.save(user);

            System.out.println("--- Khởi tạo dữ liệu Users thành công. Tài khoản: admin/123, user/123 ---");
        } else {
            System.out.println("--- CSDL đã có dữ liệu người dùng, bỏ qua bước khởi tạo Users. ---");
        }
    }
}