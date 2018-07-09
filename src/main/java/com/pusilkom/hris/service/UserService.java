package com.pusilkom.hris.service;

import com.pusilkom.hris.model.UserWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {


        List<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority("ADMIN"));
        authority.add(new SimpleGrantedAuthority("KPU"));

        UserWeb user = new UserWeb("admin", "$2a$10$KYnGWD86W56IPmXDnWE5Muyq1IMxvhMNccd2reIyiSosppAwhjZQ.", authority, new HashMap<>());

        return user;
    }
}
