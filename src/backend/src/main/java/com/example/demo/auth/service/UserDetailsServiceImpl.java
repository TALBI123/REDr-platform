package com.example.demo.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.auth.principal.CustomUserDetails;
import com.example.demo.models.user.AppUser;
import com.example.demo.user.repository.AgencyManagerRepository;
import com.example.demo.user.repository.ClientRepository;
import com.example.demo.user.repository.SuperAdminRepository;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AgencyManagerRepository agencyManagerRepository;
    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));


        // i will decommente this when i tatste without it and if it works i will delete it

        // AppUser completeUser = switch (user.getRole()) {

        //     case CLIENT ->
        //         clientRepository.findByEmail(email)
        //                 .orElseThrow(() -> new UsernameNotFoundException("Client not found"));

        //     case AGENCY_MANAGER ->
        //         agencyManagerRepository.findByEmail(email)
        //                 .orElseThrow(() -> new UsernameNotFoundException("Manager not found"));

        //     case SUPER_ADMIN ->
        //         superAdminRepository.findByEmail(email)
        //                 .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        // };

        
        return CustomUserDetails.from(user);
    }
}