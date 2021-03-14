package br.com.urbainski.apipessoas.endpoint;

import static org.hamcrest.Matchers.any;
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
public class SourceEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetSource() throws Exception {

        mockMvc.perform(get("/source").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("backend", any(String.class)));
    }
}