package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.security.UserSS;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public static UserSS authenticated(){
        try {
            return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception exception){
            return null;
        }
    }

}
