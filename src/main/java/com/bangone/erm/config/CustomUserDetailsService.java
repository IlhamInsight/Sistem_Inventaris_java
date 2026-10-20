package com.bangone.erm.config;

import com.bangone.erm.models.auth.User;
import com.bangone.erm.repositories.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">>> Mencoba Login: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User kaga ada: " + username));

        String roleName = (user.getRole() != null) ? user.getRole().getName().toUpperCase() : "USER";
        
        System.out.println(">>> User Ketemu: " + user.getUsername());
        System.out.println(">>> Role dari DB: " + roleName);
        System.out.println(">>> Hash Password di DB: " + user.getPassword());

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(Collections.singleton(new SimpleGrantedAuthority(roleName)))
            .build();
    }
}