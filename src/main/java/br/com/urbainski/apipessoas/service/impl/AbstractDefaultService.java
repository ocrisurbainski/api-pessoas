package br.com.urbainski.apipessoas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.urbainski.apipessoas.domain.IDomain;
import br.com.urbainski.apipessoas.service.IService;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
public abstract class AbstractDefaultService<ENTITY extends IDomain, PK> implements IService<ENTITY, PK> {

    @Autowired
    protected JpaRepository<ENTITY, PK> repository;

    @Override
    public ENTITY insert(ENTITY entity) {

        return repository.save(entity);
    }

    @Override
    public ENTITY update(ENTITY entity) {

        return repository.save(entity);
    }

    @Override
    public boolean existsById(PK id) {

        return repository.existsById(id);
    }

    @Override
    public void delete(PK id) {

        repository.deleteById(id);
    }

    @Override
    public Optional<ENTITY> findById(PK id) {

        return repository.findById(id);
    }

    @Override
    public Page<ENTITY> findAll(Pageable pageable) {

        return repository.findAll(pageable);
    }

}