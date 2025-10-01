package vn.iotstar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import vn.iotstar.service.UserInfoDetailService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // 1. Cung cấp UserDetailsService (Service lấy User từ DB)
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoDetailService(); // Đảm bảo đã @Service trong UserInfoDetailService
    }

    // 2. Cung cấp PasswordEncoder (Để mã hóa và so sánh mật khẩu)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. Cấu hình Authentication Provider (Sử dụng DaoAuthenticationProvider)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // 4. Cấu hình HTTP Security (Phân quyền cho các đường dẫn)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    // Cho phép tất cả truy cập vào đường dẫn này (trang chủ và thêm user)
                    .requestMatchers("/home", "/users/new").permitAll() 
                    
                    // Yêu cầu quyền ROLE_ADMIN để truy cập /users/**
                    .requestMatchers("/users/**").hasRole("ADMIN") 
                    
                    // Yêu cầu xác thực cho tất cả các request còn lại
                    .anyRequest().authenticated() 
                )
                .formLogin(form -> form.defaultSuccessUrl("/home", true)) // Cho phép form login mặc định
                .logout(logout -> logout.permitAll()) // Cho phép logout
                .build();
    }
}
