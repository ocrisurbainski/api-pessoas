package br.com.urbainski.apipessoas.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
@Getter
@Setter
@ApiModel(value = "PessoaDTO", description = "DTO com os dados das pessoas")
public class PessoaDTO {

    @ApiModelProperty(value = "Identificador da pessoa")
    private Long id;
    @ApiModelProperty(value = "Nome da pessoa")
    private String nome;
    @ApiModelProperty(value = "CPF da pessoa")
    private String cpf;
    @ApiModelProperty(value = "Email da pessoa")
    private String email;
    @ApiModelProperty(
            value = "Sexo da pessoa",
            allowableValues = "M - Masculino,F - Feminino,N - Não quero me identificar")
    private String sexo;
    @ApiModelProperty(value = "Data de nascimento da pessoa")
    private LocalDate dataNascimento;
    @ApiModelProperty(value = "Naturalidade da pessoa")
    private String naturalidade;
    @ApiModelProperty(value = "Nacionalidade da pessoa")
    private String nacionalidade;

}