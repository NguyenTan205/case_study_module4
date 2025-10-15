package com.codegym.service;

import com.codegym.model.Role;
import com.codegym.model.User;
import com.codegym.repository.IRoleRepository;
import com.codegym.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void register(User user) {
        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Lấy role mặc định (ví dụ ROLE_STUDENT)
        Role roleStudent = roleRepository.findByName("ROLE_STUDENT");
        user.getRoles().add(roleStudent);

        // Lưu user (JPA sẽ tự lưu vào bảng user_role)
        userRepository.save(user);
    }

    // Phân quyền
    @Transactional
    public void updateUserRole(Long userId, String newRoleName) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Không tìm thấy user có id: " + userId));

        Role newRole = roleRepository.findByName(newRoleName);
        if (newRole == null) {
            throw new RuntimeException("Không tìm thấy role: " + newRoleName);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(newRole);
        user.setRoles(roles);

        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
