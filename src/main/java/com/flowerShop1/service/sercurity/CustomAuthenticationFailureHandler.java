package com.flowerShop1.service.sercurity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage = "Sai email hoặc mật khẩu!";

        // ✅ Bắt lỗi cụ thể từ CustomUserDetailService
        if (exception.getMessage().contains("pending")) {
            errorMessage = "⚠️ Tài khoản của bạn đang chờ phê duyệt. Vui lòng chờ admin kích hoạt.";
        } else if (exception.getMessage().contains("inactive")) {
            errorMessage = "❌ Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ admin.";
        }

        request.getSession().setAttribute("loginError", errorMessage);
        response.sendRedirect("/login?error=true");
    }
}