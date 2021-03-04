package br.com.urbainski.apipessoas.bo.impl;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.urbainski.apipessoas.domain.Pessoa;
import br.com.urbainski.apipessoas.exception.PessoaDuplicateException;
import br.com.urbainski.apipessoas.repository.PessoaRepository;

/**
 * @author cristian.urbainski
 * @since 03/03/2021
 */
@SpringBootTest
public class PessoaBOImplTest {

    @MockBean
    private PessoaRepository pessoaRepository;
    @Autowired
    private PessoaBOImpl pessoaBO;

    @Test
    public void testValidarPessoaDuplicada_pessoaNull() {

        Pessoa pessoa = null;

        pessoaBO.validarPessoaDuplicada(pessoa);
    }

    @Test
    public void testValidarPessoaDuplicada_inserindoPessoaNaoDuplicada() {

        var cpf = "54328934015";

        var pessoa = new Pessoa();
        pessoa.setCpf(cpf);

        Mockito.when(pessoaRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        pessoaBO.validarPessoaDuplicada(pessoa);
    }

    @Test
    public void testValidarPessoaDuplicada_atualizandoPessoaNaoDuplicada() {

        var cpf = "54328934015";

        var pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setCpf(cpf);

        var pessoa2 = new Pessoa();
        pessoa2.setId(1L);
        pessoa2.setCpf(cpf);

        Mockito.when(pessoaRepository.findByCpf(cpf)).thenReturn(Optional.of(pessoa2));

        pessoaBO.validarPessoaDuplicada(pessoa);
    }

    @Test
    public void testValidarPessoaDuplicada_pessoaDuplicada() {

        var cpf = "54328934015";

        var pessoa = new Pessoa();
        pessoa.setCpf(cpf);

        var pessoa2 = new Pessoa();
        pessoa2.setId(1L);
        pessoa2.setCpf(cpf);

        Mockito.when(pessoaRepository.findByCpf(cpf)).thenReturn(Optional.of(pessoa2));

        Assertions.assertThrows(PessoaDuplicateException.class, () -> pessoaBO.validarPessoaDuplicada(pessoa));
    }

}