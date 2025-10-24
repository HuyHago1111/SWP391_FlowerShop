    package com.flowerShop1.service.user.impl.UserService;

    import com.flowerShop1.dto.user.UserCreationDTO;
    import com.flowerShop1.dto.user.UserDTO;
    import com.flowerShop1.dto.user.UserSignUpDTO;
    import com.flowerShop1.entity.Order;
    import com.flowerShop1.entity.Role;
    import com.flowerShop1.entity.User;
    import com.flowerShop1.mapper.user.UserMapper;
    import com.flowerShop1.mapper.user.UserSignUpMapper;
    import com.flowerShop1.repository.OrdersRepository;
    import com.flowerShop1.repository.RoleRepository;
    import com.flowerShop1.repository.UserRepository;
    import com.flowerShop1.service.mail.MailService;
    import com.flowerShop1.service.user.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.stereotype.Service;

    import org.springframework.data.domain.Pageable;
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
        private OrdersRepository orderRepository;
//        @Override
//        public List<User> getAllUsers() {
//            return userRepository.findAll();
//        }
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

//        @Override
//        public Page<User> getAllUsers(Pageable pageable) {
//            return userRepository.findAll(pageable);
//        }

        @Override
        public Optional<User> findUserById(Integer id) {
            return userRepository.findById(id);
        }

        @Override
        public void createUser(UserCreationDTO userDTO) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Lỗi: Email này đã được sử dụng!");
            } else if (userRepository.existsByPhone(userDTO.getPhone())) {
                throw new RuntimeException("Lỗi: Số điện thoại này đã được đăng ký!");
            }


            User user = new User();
            user.setFullName(userDTO.getFullName());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setPassword(userDTO.getPassword());
            user.setAddress(userDTO.getAddress());
            user.setStatus(userDTO.getStatus());


            // Tìm và gán Role
            Role role = roleRepository.findById(userDTO.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRole(role);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }

        @Override
        public List<Order> findOrdersByUserId(Integer userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return orderRepository.findByUserOrderByOrderDateDesc(user);
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
    }
