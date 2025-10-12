package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordResetEmail(String userName, String to, String resetURL) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Password Reset Request");
            message.setText("Dear " + userName + "!\n" +
                    "Your requested a password change on Smart Meal Finder.\n" +
                    "Please click the link below to reset your password:\n" + resetURL + "\n\n" +
                    "Your link will be active for 15 minutes! If you did not ask for a password reset, please ignore this email."
            );

            this.mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while sending the password reset email: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendVerificationEmail(String userName, String to, String verificationURL) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Smart Meal Finder: Verification Request");
            message.setText("Dear " + userName + "!\n" +
                    "Your requested a password change on Smart Meal Finder.\n" +
                    "Please click the link below to reset your password:\n" + verificationURL + "\n\n" +
                    "Your link will be active for 15 days! If you dont activate your account, it will be deleted!");

            this.mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while sending the verification email: " + e.getMessage(), e);
        }
    }
}
