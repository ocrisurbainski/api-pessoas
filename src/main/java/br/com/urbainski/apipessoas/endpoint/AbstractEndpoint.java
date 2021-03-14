package br.com.urbainski.apipessoas.endpoint;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author cristian.urbainski
 * @since 23/03/2021
 */
public abstract class AbstractEndpoint {

    protected <T> ResponseEntity<T> ok(T value) {

        return ResponseEntity.ok(value);
    }

    protected ResponseEntity<?> notContent() {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public <T> ResponseEntity<T> created(T value, URI uri) {

        return ResponseEntity.created(uri).body(value);
    }

}