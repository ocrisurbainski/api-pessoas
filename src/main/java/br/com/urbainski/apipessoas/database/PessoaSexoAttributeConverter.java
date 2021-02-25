package br.com.urbainski.apipessoas.database;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;

import br.com.urbainski.apipessoas.domain.PessoaSexo;

/**
 * @author cristian.urbainski
 * @since 23/02/2021
 */
@Converter(autoApply = true)
public class PessoaSexoAttributeConverter implements AttributeConverter<PessoaSexo, String> {

    @Override
    public String convertToDatabaseColumn(PessoaSexo pessoaSexo) {

        return pessoaSexo != null ? pessoaSexo.getCodigo() : null;
    }

    @Override
    public PessoaSexo convertToEntityAttribute(String s) {

        if (StringUtils.isEmpty(s)) {

            return null;
        }

        return PessoaSexo.fromCodigo(s);
    }

}