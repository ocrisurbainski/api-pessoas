package br.com.urbainski.apipessoas.exception.handler;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.urbainski.apipessoas.exception.PessoaNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * @author cristian.urbainski
 * @since 24/02/2020
 */
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(PessoaNotFoundException.class)
    public HttpEntity<RestMessage> handlePessoaNotFoundException(
            PessoaNotFoundException clienteNotFoundException, Locale locale) {

        var messageKey = "cliente.not.found.message";

        var params = new Object[]{clienteNotFoundException.getId()};

        var message = messageSource.getMessage(messageKey, params, locale);

        return new ResponseEntity(RestMessage.of(message), HttpStatus.NOT_FOUND);
    }

}