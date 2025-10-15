package com.codegym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.codegym.model.User;
import com.codegym.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Locale locale,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            String errorMessage = messageSource.getMessage("register.username.duplicate", null, locale);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/register";
        }

        userService.register(user);

        String successMessage = messageSource.getMessage("register.success", null, locale);
        redirectAttributes.addFlashAttribute("success", successMessage);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }
}
