package com.petcomp.sweter.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String USER_QUERY = "SELECT user_name, password, active\n" +
            "FROM usr\n" +
            "WHERE user_name = ?";
    private static final String USER_ROLES_QUERY = "SELECT u.user_name, ur.roles\n" +
            "FROM usr u\n" +
            "INNER JOIN user_role ur\n" +
            "ON u.id = ur.user_id\n" +
            "WHERE u.user_name = ?";

    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity httpSec) throws Exception {
        httpSec
                .authorizeRequests()
                    .antMatchers("/", "/registration").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery(USER_QUERY)
                .authoritiesByUsernameQuery(USER_ROLES_QUERY);
    }
}
