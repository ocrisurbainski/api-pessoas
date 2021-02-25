package br.com.urbainski.apipessoas.domain;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
public enum PessoaSexo {
    MASCULINO("M"),
    FEMININO("F"),
    NAO_QUERO_ME_IDENTIFICAR("N");

    private PessoaSexo(String codigo) {

        this.codigo = codigo;
    }

    private final String codigo;

    public String getCodigo() {

        return codigo;
    }

    public static PessoaSexo fromCodigo(String codigo) {

        if (StringUtils.isEmpty(codigo)) {

            return null;
        }

        return Stream.of(PessoaSexo.values())
                .filter(pessoaSexo -> StringUtils.equals(pessoaSexo.getCodigo(), codigo))
                .findFirst().orElse(null);
    }

}