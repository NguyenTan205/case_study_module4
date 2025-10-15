package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.repository.IUserRepository;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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

    // Quản lí danh sách user
    @GetMapping("/users")
    public String listUsers(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<User> userPage = userRepository.findAll(pageable);

        List<User> users = userPage.getContent();
        Map<Long, List<String>> userRoles = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        u -> u.getRoles().stream()
                                .map(r -> r.getName())
                                .collect(Collectors.toList())
                ));

        model.addAttribute("users", users);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("search", "");
        model.addAttribute("sortBy", sortBy);
        return "admin/user-list";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("search") String search,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(defaultValue = "id") String sortBy,
                              Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<User> userPage;

        if (search == null || search.trim().isEmpty()) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.findByUsernameContainingIgnoreCase(search.trim(), pageable);
        }

        List<User> users = userPage.getContent();
        Map<Long, List<String>> userRoles = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        u -> u.getRoles().stream()
                                .map(r -> r.getName())
                                .collect(Collectors.toList())
                ));

        model.addAttribute("users", users);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);

        return "admin/user-list";
    }

    @PostMapping("/updateRole/{id}")
    public String updateRole(@PathVariable Long id, @RequestParam String role) {
        userService.updateUserRole(id, role);
        return "redirect:/admin/users?success";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users?successDelete";
    }


}
