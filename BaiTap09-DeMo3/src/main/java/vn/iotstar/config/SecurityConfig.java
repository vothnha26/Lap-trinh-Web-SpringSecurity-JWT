package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import vn.iotstar.service.UserDetailsServiceCustom;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Cho phép phân quyền bằng Annotation (@PreAuthorize)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceCustom userDetailsServiceCustom;

    // --- 1. Cung cấp Password Encoder (BCrypt) ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- 2. Cung cấp UserDetailsService (Service lấy User từ DB) ---
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsServiceCustom;
    }

    // --- 3. Cấu hình Authentication Provider ---
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // --- 4. Cấu hình HTTP Security (Phân quyền cho URL) ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // Tắt CSRF nếu bạn làm việc với API, nhưng nên bật khi dùng Form login/View
            .csrf(csrf -> csrf.disable()) 
            
            // Cấu hình quy tắc phân quyền cho các request
            .authorizeHttpRequests(auth -> auth
                // Cho phép truy cập công khai vào các đường dẫn sau:
                .requestMatchers(
                    "/", "/index", "/login", "/register", "/css/**", "/js/**" // Trang tĩnh, login, register
                ).permitAll()
                
                // Yêu cầu quyền ROLE_ADMIN để truy cập các trang quản lý Product
                // **Phân quyền dựa trên URL**
                .requestMatchers("/products/new", "/products/edit/**", "/products/delete/**").hasRole("ADMIN")

                // Tất cả các request khác phải được xác thực (đăng nhập)
                .anyRequest().authenticated()
            )

            // Cấu hình Form Login
            .formLogin(form -> form
                .loginPage("/login") // Sử dụng trang login tùy chỉnh
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true) // Chuyển về trang chủ sau khi login thành công
                .failureUrl("/login?error=true") // Nếu login thất bại
                .permitAll()
            )

            // Cấu hình Logout
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true") // Chuyển về trang login sau khi logout
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            )
            
            // Cấu hình xử lý ngoại lệ (Access Denied)
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/403") // Chuyển hướng đến trang /403 nếu không có quyền
            );
            
        return http.build();
    }
}