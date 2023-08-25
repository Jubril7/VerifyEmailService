package com.verify.services.servImpl;

import com.verify.entity.ConfirmationToken;
import com.verify.entity.Users;
import com.verify.repo.TokenRepo;
import com.verify.repo.UserRepo;
import com.verify.services.EmailService;
import com.verify.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final EmailService emailService;

    @Override
    public ResponseEntity<?> saveUser(Users users) {
        if (userRepo.existsByUserEmail(users.getUserEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        userRepo.save(users);

        ConfirmationToken confirmationToken = new ConfirmationToken(users);
        tokenRepo.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(users.getUserEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:2323/confirm-account?token=" + confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());

        return ResponseEntity.ok("Verify email by the link sent on your email address");

    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = tokenRepo.findByConfirmationToken(confirmationToken);

        if(token != null) {
            Users user = userRepo.findByUserEmailIgnoreCase(token.getUserEntity().getUserEmail());
            user.setEnabled(true);
            userRepo.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }

}
