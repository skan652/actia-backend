package com.example.air.controller;

import com.example.air.auth.LoginAlertRequest;
import com.example.air.service.EmailRequest;
import com.example.air.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest);
            return "Email sent successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
    }

    @PostMapping("/sendFailedLoginAlert")
    public String sendFailedLoginAlert(@RequestBody LoginAlertRequest request) {
        try {
            emailService.sendFailedLoginAlert(request.getEmail());
            return "Failed login alert sent successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending alert: " + e.getMessage();
        }
    }
}
