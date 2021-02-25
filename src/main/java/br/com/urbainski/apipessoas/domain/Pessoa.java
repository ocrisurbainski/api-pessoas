package br.com.urbainski.apipessoas.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author cristian.urbainski
 * @since 22/02/2021
 */
@Entity
@Table(name = "PESSOAS")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pessoa implements IDomain<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @EqualsAndHashCode.Include
    private Long id;
    @NotEmpty
    @Column(name = "NOME")
    private String nome;
    @CPF
    @NotEmpty
    @Column(name = "CPF")
    private String cpf;
    @Email
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SEXO")
    private PessoaSexo pessoaSexo;
    @NotNull
    @Column(name = "DATANASC")
    private LocalDate dataNascimento;
    @Column(name = "NATURALIDADE")
    private String naturalidade;
    @Column(name = "NACIONALIDADE")
    private String nacionalidade;

    @Override
    public Long getId() {

        return id;
    }

    @Override
    public void setId(Long id) {

        this.id = id;
    }

}