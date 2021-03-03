package br.com.urbainski.apipessoas.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CsrfEndpoint {

    private final @NonNull CookieCsrfTokenRepository repository;

    @GetMapping
    @RequestMapping("/csrf")
    public CsrfToken getToken(HttpServletRequest request) {

        return repository.generateToken(request);
    }

}