package com.ash.springsecurity.registration;

import com.ash.springsecurity.appuser.AppUser;
import com.ash.springsecurity.appuser.AppUserRole;
import com.ash.springsecurity.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;

    private final AppUserService appUserService;

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
}
