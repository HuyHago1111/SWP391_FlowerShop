package com.flowerShop1.controller.api;

import com.flowerShop1.entity.User;
import com.flowerShop1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;



    @PostMapping("/{userid}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Integer userid, @RequestBody Map<String, String> payload) {
        try {
            String newStatus = payload.get("status");
            userService.updateUserStatus(userid, newStatus);
            return ResponseEntity.ok(Map.of("message", "Status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update status: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        try {
            var user = userService.findUserById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            // Không cho xóa ADMIN
            if ("Admin".equalsIgnoreCase(user.get().getRoleName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Cannot delete admin accounts"));
            }

            userService.deleteUserById(userId);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete user: " + e.getMessage()));
        }
    }

}
