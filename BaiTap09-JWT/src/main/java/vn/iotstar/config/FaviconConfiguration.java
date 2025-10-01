package vn.iotstar.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootConfiguration
public class FaviconConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                if ("GET".equalsIgnoreCase(request.getMethod()) && "/favicon.ico".equals(request.getRequestURI())) {
                    response.setStatus(HttpStatus.NO_CONTENT.value()); // Trả về mã trạng thái 204 No Content
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/**");
    }
}
