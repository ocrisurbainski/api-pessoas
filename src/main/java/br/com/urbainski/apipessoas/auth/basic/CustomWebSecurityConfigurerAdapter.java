package br.com.urbainski.apipessoas.auth.basic;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author cristian.urbainski
 * @since 26/02/2021
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private static final String USER_DEFAULT = "admin";
    private static final String PASSWORD_DEFAULT = "123456";
    private static final String ROLE_DEFAULT = "ADMIN";
    private static final String[] AUTH_WHITELIST = {

            "/source",

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/",
            "/csrf"
    };
    private final @NonNull MyBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        var passwordEncodedUserDefault = passwordEncoder().encode(PASSWORD_DEFAULT);

        auth.inMemoryAuthentication()
                .withUser(USER_DEFAULT)
                .password(passwordEncodedUserDefault)
                .authorities(ROLE_DEFAULT);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().csrfTokenRepository(getCookieCsrfTokenRepository())
                .and().addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class)
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public CookieCsrfTokenRepository getCookieCsrfTokenRepository() {

        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

    private Filter csrfHeaderFilter() {

        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {

                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

                if (csrf != null) {

                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");

                    String token = csrf.getToken();

                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {

                        // Token is being added to the XSRF-TOKEN cookie.
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");

                        response.addCookie(cookie);
                    }
                }

                filterChain.doFilter(request, response);
            }
        };
    }

}