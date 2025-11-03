package com.flowerShop1.config;

import com.flowerShop1.service.sercurity.CustomAuthenticationFailureHandler;
import com.flowerShop1.service.sercurity.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SercurityConfig {
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private CustomAuthenticationFailureHandler customFailureHandler;

    public SercurityConfig (CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    //@Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(customUserDetailService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return new ProviderManager(provider);
    //}
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // cho đơn giản demo; bật lại CSRF khi cần
                .authorizeHttpRequests(auth -> auth
                        // Cho phép truy cập công khai các tài nguyên tĩnh, trang chủ, login, sign-up
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**", "/svg/**").permitAll()
                        .requestMatchers("/", "/login", "/logout", "/sign-up", "/forgot", "/sign-up/verify-otp", "/change-password").permitAll()
                        .requestMatchers("/flower", "/flower-list", "/product-detail/**").permitAll()
                        .requestMatchers("/cart", "/cart/addToCart", "/cart/showCart", "/cart/getlsCart", "/cart/updateQuantity", "/cart/removeItem").permitAll()

                        // Các URL yêu cầu vai trò ADMIN
                        // Giả sử bạn có một controller cho /admin/** và trang /users là dành cho admin
                        .requestMatchers("/admin/**", "/users/**").hasAnyAuthority("Admin")

                        // Các URL yêu cầu người dùng phải đăng nhập (bất kể vai trò gì)
                        .requestMatchers("/user/**", "/address/**", "/cart/checkout", "/api/payment/create", "/vnpay/returnurl").authenticated()

                        // Tất cả các yêu cầu còn lại phải được xác thực (đã đăng nhập)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                 // Trang login custom
                        .loginProcessingUrl("/login")        // URL form submit
                        .usernameParameter("email")          // Dùng field email
                        .passwordParameter("password")       // Field password
                        .failureHandler(customFailureHandler)
                        .defaultSuccessUrl("/", true)        // Khi login thành công
//                        .failureUrl("/login?error=true")     // Khi sai mật khẩu
                        .permitAll()
                )// disable default form login
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403") // 🔥 Khi truy cập sai role, redirect đến /403
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // để mở H2 console
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

}
