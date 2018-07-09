package com.pusilkom.hris.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.function.Predicate;

public class UserUtils {

    public static final String ROLE_EKSEKUTIF = "ROLE_EKSEKUTIF";
    public static final String ROLE_MANAJERDIVISI = "ROLE_MANAJERDIVISI";
    public static final String ROLE_PMO = "ROLE_PMO";
    public static final String ROLE_KARYAWAN = "ROLE_KARYAWAN";
    public static final String ROLE_SYSTEMADMIN = "ROLE_SYSTEMADMIN";


    /**
     * Get currently username of user login
     * @return username
     */
    public static String getUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public static boolean userRoleIs(String role){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch((Predicate<GrantedAuthority>) grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

}
