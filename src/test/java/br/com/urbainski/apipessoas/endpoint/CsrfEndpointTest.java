package br.com.urbainski.apipessoas.endpoint;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.urbainski.apipessoas.ApiPessoasApplication;

/**
 * @author cristian.urbainski
 * @since 07/03/2021
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = ApiPessoasApplication.class)
@AutoConfigureMockMvc
public class CsrfEndpointTest {

    private static final String UUID_REGEX = "[0-9a-fA-F]{8}(?:-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetToken() throws Exception {

        mockMvc.perform(get("/csrf").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("token", matchesPattern(UUID_REGEX)))
                .andExpect(jsonPath("parameterName", is("_csrf")))
                .andExpect(jsonPath("headerName", is("X-XSRF-TOKEN")));
    }

}