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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.urbainski.apipessoas.domain.Pessoa;
import br.com.urbainski.apipessoas.dto.PessoaDTO;
import br.com.urbainski.apipessoas.exception.PessoaNotFoundException;
import br.com.urbainski.apipessoas.service.IPessoaService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/pessoa")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PessoaEnpointV1 extends AbstractEndpoint {

    private final @NonNull IPessoaService pessoaService;
    private final @NonNull ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity insert(@RequestBody PessoaDTO pessoaDTO) {

        var pessoa = modelMapper.map(pessoaDTO, Pessoa.class);

        pessoa = pessoaService.insert(pessoa);

        var pessoaDtoResult = modelMapper.map(pessoa, PessoaDTO.class);

        var entityModel = createEntityModel(pessoaDtoResult);

        var uri = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();

        return created(entityModel, uri);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @org.springframework.lang.NonNull @PathVariable("id") Long id,
            @org.springframework.lang.NonNull @RequestBody PessoaDTO pessoaDTO) {

        var pessoa = pessoaService.findById(id).orElseThrow(() -> new PessoaNotFoundException(id));

        pessoa = modelMapper.map(pessoaDTO, Pessoa.class);

        pessoa.setId(id);

        pessoa = pessoaService.update(pessoa);

        var pessoaDtoResult = modelMapper.map(pessoa, PessoaDTO.class);

        var entityModel = createEntityModel(pessoaDtoResult);

        return ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        if (pessoaService.existsById(id)) {

            pessoaService.delete(id);

            return notContent();
        }

        throw new PessoaNotFoundException(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PessoaDTO>> findById(
            @org.springframework.lang.NonNull @PathVariable("id") Long id) {

        var pessoa = pessoaService.findById(id).orElseThrow(() -> new PessoaNotFoundException(id));

        var dto = modelMapper.map(pessoa, PessoaDTO.class);

        var entityModel = createEntityModel(dto);

        return ok(entityModel);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PessoaDTO>>> findAll(Pageable pageable) {

        var page = pessoaService.findAll(pageable);

        var listDto = page.getContent().stream()
                .map(pessoa -> {
                    var dto = modelMapper.map(pessoa, PessoaDTO.class);
                    var entityModel = createEntityModel(dto);
                    return entityModel;
                })
                .collect(Collectors.toList());

        var collectionModel = CollectionModel.of(
                listDto, linkTo(methodOn(PessoaEnpointV1.class).findAll(pageable)).withSelfRel());

        return ok(collectionModel);
    }

    private EntityModel<PessoaDTO> createEntityModel(PessoaDTO pessoaDTO) {

        var linkToFindById = linkTo(
                methodOn(PessoaEnpointV1.class).findById(pessoaDTO.getId())).withSelfRel();

        var linkToFindAll = linkTo(
                methodOn(PessoaEnpointV1.class).findAll(null)).withRel("pessoas");

        var linkToDelete = linkTo(
                methodOn(PessoaEnpointV1.class).delete(pessoaDTO.getId())).withRel("delete");

        return EntityModel.of(pessoaDTO, linkToFindById, linkToFindAll, linkToDelete);
    }

}