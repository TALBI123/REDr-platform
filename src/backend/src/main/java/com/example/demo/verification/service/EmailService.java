package com.example.demo.verification.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final SendGrid sendGrid;

    @Value("${app.sendgrid.from-email}")
    private String fromEmail;

    @Value("${app.sendgrid.from-name}")
    private String fromName;

    @Value("${app.verification.base-url}")
    private String baseUrl;

    public void sendVerificationEmail(String toEmail, String token) {
        String link = baseUrl + "/verify-email?token=" + token;

        Email from = new Email(fromEmail, fromName);
        Email to = new Email(toEmail);
        String subject = "Verify your email address";
        Content content = new Content("text/html",
                "<p>Click the link below to verify your email:</p>"
                        + "<p><a href=\"" + link + "\">Verify Email</a></p>"
                        + "<p>This link expires in 24 hours.</p>");

        Mail mail = new Mail(from, subject, to, content);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("SendGrid error " + response.getStatusCode() + ": " + response.getBody());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
