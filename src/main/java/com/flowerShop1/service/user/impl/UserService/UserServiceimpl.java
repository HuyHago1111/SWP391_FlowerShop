package com.flowerShop1.service.user.impl.UserService;

import com.flowerShop1.dto.user.UserSignUpDTO;
import com.flowerShop1.entity.User;
import com.flowerShop1.mapper.user.UserMapper;
import com.flowerShop1.mapper.user.UserSignUpMapper;
import com.flowerShop1.repository.UserRepository;
import com.flowerShop1.service.mail.MailService;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
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
            userRepository.save(userSignUpMapper.dtoToEntity(userSignUpDTO));
            return true;
        }
        return false;

    }
    public String generateOTP() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
}
