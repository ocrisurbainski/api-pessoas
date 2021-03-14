package br.com.urbainski.apipessoas.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author cristian.urbainski
 * @since 28/02/2021
 */
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    public static final String TAG_PESSOAS_V1 = "Pessoas endpoint V1";
    public static final String TAG_SOURCE = "Source endpoint";

    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(getSecurityScheme())
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.urbainski.apipessoas.endpoint"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .tags(new Tag(TAG_PESSOAS_V1, "Endpoint de pessoas V1"))
                .tags(new Tag(TAG_SOURCE, "Endpoint do código do projeto"))
                .ignoredParameterTypes(Pageable.class)
                .apiInfo(getApiInfo());
    }

    private List<SecurityScheme> getSecurityScheme() {

        return Collections.singletonList(new BasicAuth("basicAuth"));
    }

    private ApiInfo getApiInfo() {

        return new ApiInfo(
                "api-pessoas",
                "Api de pessoas",
                "0.0.3-SNAPSHOT",
                "Terms of service",
                getContact(),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }

    private Contact getContact() {

        return new Contact(
                "Cristian Elder Urbainski",
                "https://github.com/CristianUrbainski",
                "cristianurbainskips@gmail.com");
    }

}