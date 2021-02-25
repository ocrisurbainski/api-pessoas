package br.com.urbainski.apipessoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.urbainski.apipessoas.domain.Pessoa;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}