package vn.iotstar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.iotstar.entity.UserInfo;
import vn.iotstar.repository.UserInfoRepository;

import java.util.Optional;

@Service
public class UserInfoDetailService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    // Phương thức chính để tải UserDetails theo username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user trong database
        Optional<UserInfo> userInfo = repository.findByUsername(username);
        
        // Nếu không tìm thấy, ném ngoại lệ UsernameNotFoundException
        return userInfo.map(UserInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}