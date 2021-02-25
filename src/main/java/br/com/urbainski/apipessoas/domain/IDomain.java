package br.com.urbainski.apipessoas.domain;

/**
 * @author Cristian Urbainski
 * @since 22/02/2021
 */
public interface IDomain<PK> {

    PK getId();

    void setId(PK id);

}