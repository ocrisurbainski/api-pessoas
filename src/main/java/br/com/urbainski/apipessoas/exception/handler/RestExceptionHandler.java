package br.com.urbainski.apipessoas.exception.handler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.urbainski.apipessoas.exception.PessoaDuplicateException;
import br.com.urbainski.apipessoas.exception.PessoaNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * @author cristian.urbainski
 * @since 24/02/2020
 */
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(PessoaNotFoundException.class)
    public HttpEntity<RestMessage> handlePessoaNotFoundException(
            PessoaNotFoundException pessoaNotFoundException, Locale locale) {

        var messageKey = "pessoa.not.found.message";

        var params = new Object[]{pessoaNotFoundException.getId()};

        var message = messageSource.getMessage(messageKey, params, locale);

        return new ResponseEntity<RestMessage>(RestMessage.of(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PessoaDuplicateException.class)
    public HttpEntity<RestMessage> handlePessoaDuplicateException(
            PessoaDuplicateException pessoaDuplicateException, Locale locale) {

        var messageKey = "pessoa.duplicate.message";

        var params = new Object[]{pessoaDuplicateException.getCpf()};

        var message = messageSource.getMessage(messageKey, params, locale);

        return new ResponseEntity<RestMessage>(RestMessage.of(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestMessage> handleMethodArgumentNotValid(ConstraintViolationException ex, Locale locale) {

        var restMessage = createRestMessageOfFieldErrors(ex.getConstraintViolations(), locale);

        return new ResponseEntity<RestMessage>(restMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private RestMessage createRestMessageOfFieldErrors(Set<ConstraintViolation<?>> errors, Locale locale) {

        if (errors == null || errors.size() == 0) {

            return null;
        }

        var mapErrors = new HashMap<String, String>();

        errors.stream().forEach(constraintViolation -> {

            var property = constraintViolation.getPropertyPath().toString();

            mapErrors.put(property, constraintViolation.getMessage());
        });

        return RestMessage.of(mapErrors);
    }

}