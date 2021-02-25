package br.com.urbainski.apipessoas.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.urbainski.apipessoas.domain.IDomain;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
public interface IService<ENTITY extends IDomain, PK> {

    ENTITY insert(ENTITY entity);

    ENTITY update(ENTITY entity);

    boolean existsById(PK id);

    void delete(PK id);

    Optional<ENTITY> findById(PK id);

    Page<ENTITY> findAll(Pageable pageable);

}