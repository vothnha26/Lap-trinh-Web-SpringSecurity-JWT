// File: vn.iotstar.service.UserDetailsCustom.java
package vn.iotstar.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.iotstar.entity.Users;

import java.util.Collection;
import java.util.stream.Collectors;

// Class này implement UserDetails để cung cấp thông tin cho Spring Security
public class UserDetailsCustom implements UserDetails {

    private final Users user;

    public UserDetailsCustom(Users user) {
        this.user = user;
    }

    /**
     * Chuyển đổi Set<Role> thành Collection<GrantedAuthority>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                // Tên Role phải bắt đầu bằng tiền tố ROLE_ (ví dụ: "ROLE_ADMIN")
                .map(role -> new SimpleGrantedAuthority(role.getName())) 
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        // Mật khẩu đã được mã hóa BCrypt
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Trạng thái được kích hoạt của người dùng
        return user.getEnabled(); 
    }
}