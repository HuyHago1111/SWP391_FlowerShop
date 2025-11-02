package com.flowerShop1.service.sercurity;

import com.flowerShop1.entity.User;
import com.flowerShop1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService  implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // ✅ Chặn nếu người dùng đang ở trạng thái "Pending"
        if ("Pending".equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("User is pending approval");
        }
        return new CustomUserDetails(user);
    }
}
