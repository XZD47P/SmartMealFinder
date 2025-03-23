package hu.project.smartmealfinderb.Service;

public interface EmailService {

    void sendPasswordResetEmail(String userName, String to, String resetURL);
}
