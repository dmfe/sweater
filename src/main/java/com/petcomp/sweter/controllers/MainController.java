package com.petcomp.sweter.controllers;

import com.petcomp.sweter.domain.Message;
import com.petcomp.sweter.domain.User;
import com.petcomp.sweter.repositories.MessagesRepository;
import com.petcomp.sweter.utils.TrowingConsumer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MessagesRepository messagesRepository;

    @Value("${upload.path}")
    private String uploadPath;

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
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam MultipartFile file,
            Model model) {

        Message message = new Message(text, tag, user);

        getFileName(file).ifPresent(TrowingConsumer.unchecked(name -> {
            file.transferTo(new File(uploadPath + "/" + name));
            message.setFilename(name);
        }));

        messagesRepository.save(message);

        model.addAttribute("messages", messagesRepository.findAll());

        return "root";
    }

    private Optional<String> getFileName(MultipartFile file) {
        if (isFilePresent(file)) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidPrefix = UUID.randomUUID().toString();
            return Optional.of(uuidPrefix + "." + file.getOriginalFilename());
        }

        return Optional.empty();
    }

    private static boolean isFilePresent(MultipartFile file) {
        return file != null && StringUtils.isNotEmpty(file.getOriginalFilename());
    }

    private Iterable<Message> filterMessages(String filter) {
        return  StringUtils.isEmpty(filter) ? messagesRepository.findAll() : messagesRepository.findByTag(filter);
    }
}
