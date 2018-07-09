/*
package com.pusilkom.hris.controller;

import com.pusilkom.hris.service.ExtendedCasAssertionUserDetailService;
import com.pusilkom.hris.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Controller
@ControllerAdvice
public class SiteController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('GET_')")
    public String index(Principal principal) {
        System.out.println("User : " + principal.getName());
        return "site/index";
    }

    @GetMapping("/signin")
    public String login() {
        return "site/login";
    }

    @ModelAttribute("loggedInUser")
    public String getLoggedInUser(@NotNull Authentication auth) {
        return "jakarta";
    }
    @ModelAttribute("loggedInTitle")
    public String getLoggedInTitle(@NotNull Authentication auth) {
        return "Operator";
    }
}
*/
