// File: vn.iotstar.service.UserDetailsServiceCustom.java
package vn.iotstar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.Users;
import vn.iotstar.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Phương thức chính được gọi bởi Spring Security để tải thông tin người dùng
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. Tìm User từ database
        Optional<Users> optionalUser = userRepository.findByUsername(username);

        // 2. Nếu không tìm thấy, ném ra ngoại lệ
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // 3. Nếu tìm thấy, chuyển đổi sang đối tượng UserDetailsCustom
        Users user = optionalUser.get();
        return new UserDetailsCustom(user);
    }
}