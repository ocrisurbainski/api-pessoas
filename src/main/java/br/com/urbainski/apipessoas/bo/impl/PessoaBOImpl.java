package br.com.urbainski.apipessoas.bo.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.urbainski.apipessoas.bo.PessoaBO;
import br.com.urbainski.apipessoas.domain.Pessoa;
import br.com.urbainski.apipessoas.exception.PessoaDuplicateException;
import br.com.urbainski.apipessoas.repository.PessoaRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author cristian.urbainski
 * @since 03/03/2021
 */
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PessoaBOImpl implements PessoaBO {

    private final @NonNull PessoaRepository repository;

    @Override
    public void validarPessoaDuplicada(Pessoa pessoa) {

        if (pessoa == null || StringUtils.isEmpty(pessoa.getCpf())) {

            return;
        }

        var optional = repository.findByCpf(pessoa.getCpf());

        optional.ifPresent(pessoaEncontrada -> {

            boolean isSameId = pessoaEncontrada.getId().equals(pessoa.getId());

            if (!isSameId) {

                throw new PessoaDuplicateException(pessoa.getCpf());
            }
        });
    }

}