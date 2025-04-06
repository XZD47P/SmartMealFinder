package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public void sendPasswordResetEmail(String userName, String to, String resetURL) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Dear " + userName + "!\n" +
                "Your requested a password change on Smart Meal Finder.\n" +
                "Please click the link below to reset your password:\n" + resetURL + "\n\n" +
                "Your link will be active for 15 minutes! If you did not ask for a password reset, please ignore this email."
        );

        this.mailSender.send(message);
    }

    @Override
    public void sendVerificationEmail(String userName, String to, String verificationURL) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Smart Meal Finder: Verification Request");
        message.setText("Dear " + userName + "!\n" +
                "Your requested a password change on Smart Meal Finder.\n" +
                "Please click the link below to reset your password:\n" + verificationURL + "\n\n" +
                "Your link will be active for 15 days! If you dont activate your account, it will be deleted!");

        this.mailSender.send(message);
    }
}
