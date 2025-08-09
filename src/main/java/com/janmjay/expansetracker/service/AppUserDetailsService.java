//package com.janmjay.expansetracker.service;
//
//import com.janmjay.expansetracker.entity.ProfileEntity;
//import com.janmjay.expansetracker.repository.ProfileRepository;
//import jakarta.persistence.Column;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//@RequiredArgsConstructor
//public class AppUserDetailsService implements UserDetailsService {
//
//    private final ProfileRepository profileRepository;
//
//    // This method is responsible for loading the data from Database and going to find the profile based on email address
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        ProfileEntity existingProfile = profileRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
//        return User.builder()
//                .username(existingProfile.getEmail())
//                .password(existingProfile.getPassword())
//                .authorities(Collections.emptyList())
//                .build();
//    }
//
//}


package com.janmjay.expansetracker.service;

import com.janmjay.expansetracker.entity.ProfileEntity;
import com.janmjay.expansetracker.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("AppUserDetailsService: Loading user by email: " + email);

        ProfileEntity profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.err.println("User not found with email: " + email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        System.out.println("User found: " + profile.getEmail() + ", Active: " + profile.getIsActive());

        // Check if account is active
        if (!profile.getIsActive()) {
            System.err.println("Account is not active for email: " + email);
            throw new org.springframework.security.authentication.DisabledException("Account is not activated");
        }

        // Return UserDetails object
        return User.builder()
                .username(profile.getEmail())
                .password(profile.getPassword()) // This should be the encoded password from database
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!profile.getIsActive())
                .build();
    }
}
