package br.com.urbainski.apipessoas.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cristian.urbainski
 * @since 07/03/2021
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "PessoaResponseDTO", description = "DTO com os dados de respostas das pessoas")
public class PessoaResponseDTO extends PessoaCadastroDTO {

    @ApiModelProperty(value = "Identificador da pessoa")
    private Long id;

    public PessoaResponseDTO(final Long id, final String nome, final String cpf, final String email, final String sexo,
            final LocalDate dataNascimento, final String naturalidade, final String nacionalidade) {

        super(nome, cpf, email, sexo, dataNascimento, naturalidade, nacionalidade);
        this.id = id;
    }

}