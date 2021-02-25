package br.com.urbainski.apipessoas.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
@Getter
@Setter
public class PessoaDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String sexo;
    private LocalDate dataNascimento;
    private String naturalidade;
    private String nacionalidade;

}