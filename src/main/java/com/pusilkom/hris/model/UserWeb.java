package com.pusilkom.hris.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class UserWeb extends User {
    private Map<String, Object> attributes;
    private List<String> strRoles;

    public UserWeb(String username, String password, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attribute) {
        super(username, password, authorities);
        this.attributes = attribute;
        strRoles = new ArrayList<>();

        for(GrantedAuthority auth : authorities) {
            if(auth.toString().length() >= 5 && auth.toString().substring(0, 5).equalsIgnoreCase("ROLE_")) {
                strRoles.add(auth.toString());
            }
        }
    }

    public UserWeb(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        strRoles = new ArrayList<>();

        for(GrantedAuthority auth : authorities) {
            if(auth.toString().length() >= 5 && auth.toString().substring(0, 5).equalsIgnoreCase("ROLE_")) {
                strRoles.add(auth.toString());
            }
        }
    }
}
