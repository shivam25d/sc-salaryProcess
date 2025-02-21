package com.sc.salary.process.service;

import com.sc.salary.process.exception.handler.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.from:babyshark682004@gmail.com}")  // Default email if not configured
    private String fromEmail;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachment(String to, String subject, String body, String pdfFilePath) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            // Attach file if the path is valid
            File pdfFile = new File(pdfFilePath);
            if (pdfFile.exists() && pdfFile.isFile()) {
                FileSystemResource fileResource = new FileSystemResource(pdfFile);
                helper.addAttachment("Salary_Slip.pdf", fileResource);
                logger.info("Attachment added: {}", pdfFilePath);
            } else {
                logger.warn("Attachment file not found or is invalid: {}", pdfFilePath);
            }

            // Send email
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send email with attachment to {}", to, e);
            throw new EmailSendingException("Failed to send email with attachment", e);
        }
    }
}
