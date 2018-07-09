/*
package com.pusilkom.hris.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("dataSource")
    @Autowired
    DataSource ds;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        String idForEncode = "bcrypt";
//        Map encoders = new HashMap<>();
//        encoders.put("", NoOpPasswordEncoder.getInstance());
//        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);

        auth.jdbcAuthentication().dataSource(ds)
                .usersByUsernameQuery(
                        "SELECT username, CONCAT('{noop}',password) AS password, enabled as enable FROM mpp.\"PENGGUNA\" "
                                + "WHERE username= ?")
                .authoritiesByUsernameQuery(
                        "SELECT username, nama_role AS role FROM " +
                                "mpp.\"PENGGUNA\" JOIN mpp.\"ROLE_PENGGUNA\" ON mpp.\"PENGGUNA\".id=mpp.\"ROLE_PENGGUNA\".id_pengguna " +
                                "JOIN mpp.\"ROLE\" ON mpp.\"ROLE\".id=mpp.\"ROLE_PENGGUNA\".id_pengguna "
                                + "WHERE username= ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/pmo/**").permitAll()
                .antMatchers("/proyek/**").permitAll()
                .antMatchers("/eksekutif/**").permitAll()
                .antMatchers("/admin/**").permitAll()
                .antMatchers("/karyawan/**").permitAll()
                .antMatchers("/mngdivisi/**").permitAll()*/
/*.hasRole("MANAJERDIVISI")*//*

                .antMatchers("/rekap/**").permitAll()
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                .and()
                    .csrf().disable();



    }

}
*/
