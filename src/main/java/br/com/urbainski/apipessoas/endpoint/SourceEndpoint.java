package br.com.urbainski.apipessoas.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.urbainski.apipessoas.config.SwaggerConfiguration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author cristian.urbainski
 * @since 27/02/2021
 */
@RestController
@RequestMapping("/source")
@Api(tags = {SwaggerConfiguration.TAG_SOURCE})
public class SourceEndpoint extends AbstractEndpoint {

    @GetMapping
    @ApiOperation(value = "Devolve a url do repositório do projeto no GitHub.")
    public ResponseEntity<Map<String, String>> getSource() {

        var map = new HashMap<String, String>();
        map.put("backend", "https://github.com/CristianUrbainski/api-pessoas");

        return ok(map);
    }

}