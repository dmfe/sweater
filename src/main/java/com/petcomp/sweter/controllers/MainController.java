package com.petcomp.sweter.controllers;

import com.petcomp.sweter.domain.Message;
import com.petcomp.sweter.domain.User;
import com.petcomp.sweter.repositories.MessagesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MessagesRepository messagesRepository;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String root(Map<String, Object> model) {
        model.put("messages", messagesRepository.findAll());
        return "root";
    }

    @PostMapping("/main")
    public String addMessage(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model) {

        messagesRepository.save(new Message(text, tag, user));

        model.put("messages", messagesRepository.findAll());

        return "root";
    }

    @PostMapping("filter")
    public String filterMessages(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Message> messages = StringUtils.isEmpty(filter) ? messagesRepository.findAll() :
                messagesRepository.findByTag(filter);

        model.put("messages", messages);

        return "root";
    }
}
