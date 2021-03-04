package br.com.urbainski.apipessoas.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cristian.urbainski
 * @since 03/03/2021
 */
@AllArgsConstructor
public class PessoaDuplicateException extends RuntimeException {

    @Getter
    private String cpf;

}