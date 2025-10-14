package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.repository.IUserRepository;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String adminHome() {
        return "admin/home";
    }

    // Danh sách user
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();

        // Tạo Map userId -> danh sách tên quyền
        Map<Long, List<String>> userRoles = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        u -> u.getRoles().stream()
                                .map(role -> role.getName())
                                .collect(Collectors.toList())
                ));

        model.addAttribute("users", users);
        model.addAttribute("userRoles", userRoles);
        return "admin/user-list";
    }

    // Cập nhật phân quyền cho user
    @PostMapping("/updateRole/{id}")
    public String updateRole(@PathVariable Long id, @RequestParam String role) {
        userService.updateUserRole(id, role);
        return "redirect:/admin/users?success";
    }
}
