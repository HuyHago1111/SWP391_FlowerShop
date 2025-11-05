// src/main/java/com/flowerShop1/service/user/impl/UserService/UserServiceimpl.java

package com.flowerShop1.service.user.impl.UserService;

import com.flowerShop1.dto.user.UserCreationDTO;
import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.entity.Order;
import com.flowerShop1.entity.Role;
import com.flowerShop1.entity.User;
import com.flowerShop1.mapper.user.UserMapper;
import com.flowerShop1.mapper.user.UserSignUpMapper;
import com.flowerShop1.repository.OrderRepository;
import com.flowerShop1.repository.RoleRepository;
import com.flowerShop1.repository.UserRepository;
import com.flowerShop1.service.mail.MailService;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSignUpMapper userSignUpMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int userId) {
        System.out.println("[DEBUG] Fetching user with ID: " + userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    @Override
    public void save(User user) {
        System.out.println("[DEBUG] Saving user: " + user);
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(UserSignUpDTO userSignUpDTO) {

        String otp = generateOTP();
        userSignUpDTO.setOtp(otp);
        userSignUpDTO.setOtpExprirationTime(java.time.LocalDateTime.now().plusMinutes(3));

        mailService.sendOTP(userSignUpDTO.getEmail(), otp);
        //hàm random otp 6 số ngẫu nhiên
    }

    @Override
    public void register(UserSignUpDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        UserSignUpDTO dto = new UserSignUpDTO();

        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpExprirationTime(java.time.LocalDateTime.now().plusMinutes(3));

        mailService.sendOTP(user.getEmail(), otp);
        //hàm random otp 6 số ngẫu nhiên

    }
    @Override
    public boolean verifyOTP(String otp, UserSignUpDTO userSignUpDTO){
        if (userSignUpDTO.getOtp().equals(otp) && userSignUpDTO.getOtpExprirationTime().isAfter(java.time.LocalDateTime.now())){
            //Chỉ lưu khi đăng ký, không lưu khi quên mật khẩu
            if(userRepository.findByEmail(userSignUpDTO.getEmail()) == null) {
                userRepository.save(userSignUpMapper.dtoToEntity(userSignUpDTO));
            }
            return true;
        }
        return false;
    }

    public void updateStatus(Integer userId, String newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(newStatus);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override

    public Page<User> searchUsers(String keyword, String status, Pageable pageable) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all");

        if (hasKeyword && hasStatus) {
            return userRepository.findByFullNameContainingIgnoreCaseAndStatusIgnoreCase(keyword, status, pageable);
        } else if (hasKeyword) {
            return userRepository.findByFullNameContainingIgnoreCase(keyword, pageable);
        } else if (hasStatus) {
            return userRepository.findByStatusIgnoreCase(status, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

        @Override
        public Page<User> searchByKeywordAndRole(String keyword, String role, Pageable pageable) {
            if (keyword == null) keyword = "";
            if (role == null) role = "all";
            return userRepository.searchByKeywordAndRole(keyword, role, pageable);
        }


    @Override
    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserCreationDTO userDTO) {
        return null;
    }

    @Override
    public void updateUserRole(Integer userId, Integer newRoleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role newRole = roleRepository.findById(newRoleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(newRole);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public List<Order> findOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }


    public void updateUserStatus(Integer userId, String newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(newStatus);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }


    public String generateOTP() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

     // ✅ TÁI CẤU TRÚC LOGIC GỬI OTP VÀO MỘT HÀM RIÊNG
    private void generateAndSendOtp(UserSignUpDTO user) {
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpExprirationTime(java.time.LocalDateTime.now().plusMinutes(3)); // OTP hiệu lực trong 3 phút
        mailService.sendOTP(user.getEmail(), otp);
    }

   // ✅ TRIỂN KHAI PHƯƠNG THỨC MỚI
    @Override
    public void resendOtp(UserSignUpDTO userSignUpDTO) {
        // Đơn giản là gọi lại hàm helper để tạo và gửi OTP mới
        generateAndSendOtp(userSignUpDTO);
    }
}