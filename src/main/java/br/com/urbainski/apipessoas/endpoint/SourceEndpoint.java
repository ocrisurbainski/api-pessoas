package br.com.urbainski.apipessoas.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cristian.urbainski
 * @since 27/02/2021
 */
@RestController
@RequestMapping("/source")
public class SourceEndpoint extends AbstractEndpoint {

    @GetMapping
    public ResponseEntity<String> source() {

        return ok("https://github.com/CristianUrbainski/api-pessoas");
    }

}