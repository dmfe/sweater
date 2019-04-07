package com.petcomp.sweter.controllers;

import com.petcomp.sweter.domain.Role;
import com.petcomp.sweter.domain.User;
import com.petcomp.sweter.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepository userRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {

        User persistedUser = userRepository.findByUserName(user.getUserName());

        if (persistedUser != null) {
            model.put("message", "User already exists.");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return "redirect:/login";
    }
}
