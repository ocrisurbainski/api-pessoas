package br.com.urbainski.apipessoas.exception;

import javax.persistence.NoResultException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cristian.urbainski
 * @since 24/02/2020
 */
@AllArgsConstructor
public class PessoaNotFoundException extends NoResultException {

    @Getter
    private Long id;

}