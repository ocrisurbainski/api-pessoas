package br.com.urbainski.apipessoas.bo;

import br.com.urbainski.apipessoas.domain.Pessoa;

/**
 * @author cristian.urbainski
 * @since 03/03/2021
 */
public interface PessoaBO {

    void validarPessoaDuplicada(Pessoa pessoa);

}