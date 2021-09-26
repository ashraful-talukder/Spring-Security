package com.ash.springsecurity.registration;

import com.ash.springsecurity.appuser.AppUser;
import com.ash.springsecurity.appuser.AppUserRole;
import com.ash.springsecurity.appuser.AppUserService;
import com.ash.springsecurity.registration.token.ConfirmationToken;
import com.ash.springsecurity.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {

        boolean isValidEmail =  emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }
        return appUserService.signUpUser(new AppUser(request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER));
    }

    @Transactional
    public  String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed!");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired!");
        }

        confirmationTokenService.setConfirmation(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        return "confirmed!";
    }
}
