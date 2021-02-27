package br.com.urbainski.apipessoas.auth.basic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author cristian.urbainski
 * @since 26/02/2021
 */
@Slf4j
@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private static final String REALM_NAME ="api-pessoas";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        PrintWriter writer = response.getWriter();

        writer.println("HTTP Status 401 - " + authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() {

        log.info("afterPropertiesSet");

        setRealmName(REALM_NAME);

        super.afterPropertiesSet();
    }

}