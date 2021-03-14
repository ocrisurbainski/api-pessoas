package br.com.urbainski.apipessoas.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PessoaCadastroDTO", description = "DTO com os dados para cadastro das pessoas")
public class PessoaCadastroDTO {

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