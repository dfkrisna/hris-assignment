package com.pusilkom.hris.config;

import com.kakawait.spring.boot.security.cas.CasAuthenticationProviderSecurityBuilder;
import com.kakawait.spring.boot.security.cas.CasSecurityConfigurerAdapter;
import com.kakawait.spring.boot.security.cas.CasSecurityProperties;
import com.pusilkom.hris.service.AuthItemService;
import com.pusilkom.hris.service.ExtendedCasAssertionUserDetailService;
import com.pusilkom.hris.service.AuthItemService;
import com.pusilkom.hris.service.ExtendedCasAssertionUserDetailService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@Data
public class CustomCasSecurityConfiguration extends CasSecurityConfigurerAdapter {
    Logger logger = LoggerFactory.getLogger(getClass());

    private CasSecurityProperties securityProperties;
    private CasAuthenticationProvider casAuthenticationProvider;

    @Autowired
    AuthItemService authItemService;

    public CustomCasSecurityConfiguration(CasSecurityProperties casSecurityProperties) {
        this.securityProperties = casSecurityProperties;
    }

    @Override
    public void configure(CasAuthenticationProviderSecurityBuilder provider) {
        logger.debug("===CustomCasSecurityConfiguration===");
        try {
            Set<SimpleGrantedAuthority> authorities = Arrays.stream(securityProperties.getUser().getDefaultRoles())
                    .map(r -> r.replaceFirst("^ROLE_", ""))
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toSet());
            String[] attributes = securityProperties.getUser().getRolesAttributes();
            provider.authenticationUserDetailsService(new ExtendedCasAssertionUserDetailService(authItemService, attributes, authorities));
            casAuthenticationProvider = provider.build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/css/**", "/font-awesome/**", "/fonts/**", "/img/**", "/js/**").permitAll()
//                .anyRequest().fullyAuthenticated()
//                .and()
//                .formLogin().loginPage("/signin").permitAll();
//    }


}
