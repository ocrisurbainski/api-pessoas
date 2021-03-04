package br.com.urbainski.apipessoas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.urbainski.apipessoas.bo.impl.PessoaBOImpl;
import br.com.urbainski.apipessoas.domain.Pessoa;
import br.com.urbainski.apipessoas.service.IPessoaService;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PessoaServiceImpl extends AbstractDefaultService<Pessoa, Long> implements IPessoaService {

    private final @NonNull PessoaBOImpl boPessoa;

    @Override
    protected void preInsert(Pessoa pessoa) {

        boPessoa.validarPessoaDuplicada(pessoa);
    }

    @Override
    protected void preUpdate(Pessoa pessoa) {

        boPessoa.validarPessoaDuplicada(pessoa);
    }

}