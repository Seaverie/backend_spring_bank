package com.banking.demo.security;

import com.banking.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(java.lang.String username) throws org.springframework.security.core.userdetails.UsernameNotFoundException{
        return userRepository.findByUsername(username).orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User Not Found: " + username));
    }
}
