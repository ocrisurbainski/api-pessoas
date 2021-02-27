package br.com.urbainski.apipessoas.auth.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author cristian.urbainski
 * @since 26/02/2021
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor =  @__(@Autowired))
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private static final String USER_DEFAULT = "admin";
    private static final String PASSWORD_DEFAULT = "123456";
    private static final String ROLE_DEFAULT = "ADMIN";
    private final @NonNull MyBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        var passwordEncodedUserDefault = passwordEncoder().encode(PASSWORD_DEFAULT);

        auth.inMemoryAuthentication()
                .withUser(USER_DEFAULT)
                .password(passwordEncodedUserDefault)
                .authorities(ROLE_DEFAULT);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/source").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);
    }

}