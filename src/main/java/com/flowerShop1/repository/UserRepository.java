package com.flowerShop1.repository;

import com.flowerShop1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Page<User> findByFullNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<User> findByFullNameContainingIgnoreCaseAndStatusIgnoreCase(String keyword, String status, Pageable pageable);
    // 🔍 Chỉ lọc trạng thái

    Page<User> findByStatusIgnoreCase(String status, Pageable pageable);
//    Page<User> getAllUsers(Pageable pageable);
    Page<User> findByRole_RoleNameIgnoreCase(String roleName, Pageable pageable);
    Page<User> findByRole_RoleNameIgnoreCaseAndFullNameContainingIgnoreCase(String roleName, String keyword, Pageable pageable);
    @Query("""
        SELECT u FROM User u
        WHERE 
            (:keyword IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND 
            (LOWER(u.role.roleName) IN ('staff', 'shipper'))
        AND 
            (:role = 'all' OR LOWER(u.role.roleName) = LOWER(:role))
        ORDER BY u.userId ASC
        """)
    Page<User> searchByKeywordAndRole(
            @Param("keyword") String keyword,
            @Param("role") String role,
            Pageable pageable
    );
}
