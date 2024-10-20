package com.dev.controller;

import com.dev.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {

    EmailService emailService;

//    @GetMapping("/send")
//    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) throws MessagingException {
//        emailService.sendSimpleMessage(to, subject, body);
//        return "Email sent successfully!";
//    }

}
