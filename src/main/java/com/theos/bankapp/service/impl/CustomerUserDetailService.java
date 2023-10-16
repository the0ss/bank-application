package com.theos.bankapp.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.theos.bankapp.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException(username+"notfound"));
    }
    
}
