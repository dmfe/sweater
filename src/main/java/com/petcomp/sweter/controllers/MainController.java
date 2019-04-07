package com.petcomp.sweter.controllers;

import com.petcomp.sweter.domain.Message;
import com.petcomp.sweter.domain.User;
import com.petcomp.sweter.repositories.MessagesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MessagesRepository messagesRepository;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String root(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        model.addAttribute("messages", filterMessages(filter));
        model.addAttribute("filter", filter);

        return "root";
    }

    @PostMapping("/main")
    public String addMessage(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam String text,
            @RequestParam String tag,
            Model model) {

        messagesRepository.save(new Message(text, tag, user));

        model.addAttribute("messages", filterMessages(filter));
        model.addAttribute("filter", filter);

        return "root";
    }

    private Iterable<Message> filterMessages(String filter) {
        return  StringUtils.isEmpty(filter) ? messagesRepository.findAll() : messagesRepository.findByTag(filter);
    }
}
