package br.com.urbainski.apipessoas;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.urbainski.apipessoas.domain.PessoaSexo;

@SpringBootApplication
public class ApiPessoasApplication {

    public static void main(String[] args) {

        SpringApplication.run(ApiPessoasApplication.class, args);
    }

    @Bean
    public ObjectMapper getObjectMapper() {

        var objectMapper = new ObjectMapper();

        objectMapper.setSerializationInclusion(Include.NON_NULL);

        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    @Bean
    public ModelMapper getModelMapper() {

        var mapper = new ModelMapper();

        mapper.addConverter(new Converter<String, PessoaSexo>() {
            @Override
            public PessoaSexo convert(MappingContext<String, PessoaSexo> mappingContext) {

                var source = mappingContext.getSource();
                return StringUtils.isEmpty(source) ? null : PessoaSexo.fromCodigo(source);
            }
        });

        mapper.addConverter(new Converter<PessoaSexo, String>() {
            @Override
            public String convert(MappingContext<PessoaSexo, String> mappingContext) {

                var source = mappingContext.getSource();
                return source == null ? null : source.getCodigo();
            }
        });

        return mapper;
    }

}