package br.com.urbainski.apipessoas.endpoint;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.urbainski.apipessoas.annotations.ApiPageable;
import br.com.urbainski.apipessoas.config.SwaggerConfiguration;
import br.com.urbainski.apipessoas.domain.Pessoa;
import br.com.urbainski.apipessoas.dto.PessoaCadastroDTO;
import br.com.urbainski.apipessoas.dto.PessoaResponseDTO;
import br.com.urbainski.apipessoas.exception.PessoaNotFoundException;
import br.com.urbainski.apipessoas.service.IPessoaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/pessoa")
@Api(tags = {SwaggerConfiguration.TAG_PESSOAS_V1})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PessoaEnpointV1 extends AbstractEndpoint {

    private final @NonNull IPessoaService pessoaService;
    private final @NonNull ModelMapper modelMapper;

    @PostMapping
    @ApiOperation(value = "Método utilizado para inserir uma nova pessoa.", authorizations = {
            @Authorization("basicAuth")})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pessoa cadastrada com sucesso"),
            @ApiResponse(code = 401, message = "Caso não tenha sido feita a autenticação"),
            @ApiResponse(code = 500, message = "Caso a pessoa com o CPF informado já exista, ou algum outro problema com os dados")
    })
    public ResponseEntity<EntityModel<PessoaResponseDTO>> insert(@RequestBody PessoaCadastroDTO pessoaCadastroDTO) {

        var pessoa = modelMapper.map(pessoaCadastroDTO, Pessoa.class);

        pessoa = pessoaService.insert(pessoa);

        var pessoaResponseDTO = modelMapper.map(pessoa, PessoaResponseDTO.class);

        var entityModel = createEntityModel(pessoaResponseDTO);

        var uri = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();

        return created(entityModel, uri);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Método utilizado para atualizar uma pessoa.", authorizations = {@Authorization("basicAuth")})
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pessoa atualizada com sucesso"),
            @ApiResponse(code = 401, message = "Caso não tenha sido feita a autenticação"),
            @ApiResponse(code = 500, message = "Caso a pessoa com o CPF informado já exista, ou algum outro problema com os dados")
    })
    public ResponseEntity<EntityModel<PessoaResponseDTO>> update(
            @org.springframework.lang.NonNull @PathVariable("id") Long id,
            @org.springframework.lang.NonNull @RequestBody PessoaCadastroDTO pessoaCadastroDTO) {

        var pessoa = pessoaService.findById(id).orElseThrow(() -> new PessoaNotFoundException(id));

        pessoa = modelMapper.map(pessoaCadastroDTO, Pessoa.class);

        pessoa.setId(id);

        pessoa = pessoaService.update(pessoa);

        var pessoaResponseDTO = modelMapper.map(pessoa, PessoaResponseDTO.class);

        var entityModel = createEntityModel(pessoaResponseDTO);

        return ok(entityModel);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Método utilizado para remover uma pessoa.", authorizations = {@Authorization("basicAuth")})
    @ApiImplicitParam(name = "id", dataType = "long", defaultValue = "1", required = true, paramType = "path")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(code = 204, message = "Pessoa deletada com sucesso"),
            @ApiResponse(code = 401, message = "Caso não tenha sido feita a autenticação"),
            @ApiResponse(code = 404, message = "Caso a pessoa não seja encontrada")
    })
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (pessoaService.existsById(id)) {

            pessoaService.delete(id);

            return notContent();
        }

        throw new PessoaNotFoundException(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Método utilizado para recuperar os dados de uma pessoa pelo seu identificador.", authorizations = {
            @Authorization("basicAuth")})
    @ApiImplicitParam(name = "id", dataType = "long", defaultValue = "1", required = true, paramType = "path")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Caso não tenha sido feita a autenticação"),
            @ApiResponse(code = 404, message = "Caso a pessoa não seja encontrada")
    })
    public ResponseEntity<EntityModel<PessoaResponseDTO>> findById(
            @org.springframework.lang.NonNull @PathVariable("id") Long id) {

        var pessoa = pessoaService.findById(id).orElseThrow(() -> new PessoaNotFoundException(id));

        var pessoaResponseDTO = modelMapper.map(pessoa, PessoaResponseDTO.class);

        var entityModel = createEntityModel(pessoaResponseDTO);

        return ok(entityModel);
    }

    @GetMapping
    @ApiOperation(value = "Método utilizado para pesquisar as pessoas cadastradas.", authorizations = {
            @Authorization("basicAuth")})
    @ApiPageable
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Caso não tenha sido feita a autenticação")
    })
    public ResponseEntity<CollectionModel<EntityModel<PessoaResponseDTO>>> findAll(Pageable pageable) {

        var page = pessoaService.findAll(pageable);

        var listDto = page.getContent().stream()
                .map(pessoa -> {
                    var pessoaResponseDTO = modelMapper.map(pessoa, PessoaResponseDTO.class);
                    var entityModel = createEntityModel(pessoaResponseDTO);
                    return entityModel;
                })
                .collect(Collectors.toList());

        var collectionModel = CollectionModel.of(
                listDto, linkTo(methodOn(PessoaEnpointV1.class).findAll(pageable)).withSelfRel());

        return ok(collectionModel);
    }

    private EntityModel<PessoaResponseDTO> createEntityModel(PessoaResponseDTO pessoaResponseDTO) {

        var linkToFindById = linkTo(
                methodOn(PessoaEnpointV1.class).findById(pessoaResponseDTO.getId())).withSelfRel();

        var linkToFindAll = linkTo(
                methodOn(PessoaEnpointV1.class).findAll(null)).withRel("pessoas");

        var linkToDelete = linkTo(
                methodOn(PessoaEnpointV1.class).delete(pessoaResponseDTO.getId())).withRel("delete");

        return EntityModel.of(pessoaResponseDTO, linkToFindById, linkToFindAll, linkToDelete);
    }

}