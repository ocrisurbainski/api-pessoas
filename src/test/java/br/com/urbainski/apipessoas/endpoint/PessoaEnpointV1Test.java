package br.com.urbainski.apipessoas.endpoint;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.urbainski.apipessoas.ApiPessoasApplication;
import br.com.urbainski.apipessoas.domain.Pessoa;
import br.com.urbainski.apipessoas.dto.PessoaCadastroDTO;
import br.com.urbainski.apipessoas.repository.PessoaRepository;

/**
 * @author cristian.urbainski
 * @since 07/03/2021
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = ApiPessoasApplication.class)
@AutoConfigureMockMvc
public class PessoaEnpointV1Test {

    private static final String URI_API = "/api/v1/pessoa";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "123456";
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PessoaRepository pessoaRepository;
    private Long idPessoa2;
    private Long idPessoa3;

    @BeforeEach
    public void setup() {

        pessoaRepository.deleteAll();

        idPessoa2 = setupInsert(getPessoaCadastroDTO2());

        idPessoa3 = setupInsert(getPessoaCadastroDTO3());

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testInsert_unauthorized() throws Exception {

        mockMvc.perform(post(URI_API).with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    public void testInsert_validateNomeNull() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setNome(null);

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.nome", any(String.class)));
    }

    @Test
    public void testInsert_validateNomeEmpty() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setNome("");

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.nome", any(String.class)));
    }

    @Test
    public void testInsert_validateNomeSize() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setNome("Fulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano "
                + "Fulano Fulano Fulano FulanoFulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano "
                + "Fulano Fulano Fulano Fulano FulanoFulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano ");

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.nome", any(String.class)));
    }

    @Test
    public void testInsert_validateCpfEmpty() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setCpf("");

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.cpf", any(String.class)));
    }

    @Test
    public void testInsert_validateCpfInvalido() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setCpf("89231042052");

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.cpf", any(String.class)));
    }

    @Test
    public void testInsert_validateEmailInvalido() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setEmail("fulano");

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.email", any(String.class)));
    }

    @Test
    public void testInsert_validateEmailSize() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setEmail(
                "fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano@gmail.com");

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.email", any(String.class)));
    }

    @Test
    public void testInsert_validateDataNascimentoNull() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setDataNascimento(null);

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.dataNascimento", any(String.class)));
    }

    @Test
    public void testInsert_validateDataNascimentoFuturo() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setDataNascimento(LocalDate.now().plusDays(1));

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.dataNascimento", any(String.class)));
    }

    @Test
    public void testInsert_cpfDuplicado() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setCpf("71954728093");

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", any(String.class)));
    }

    @Test
    public void testInsert_ok() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("nome", equalTo("Fulano Alberto")))
                .andExpect(jsonPath("cpf", equalTo("89231042050")))
                .andExpect(jsonPath("email", equalTo("fulano@gmail.com")))
                .andExpect(jsonPath("sexo", equalTo("M")))
                .andExpect(jsonPath("dataNascimento", equalTo("2000-01-02")))
                .andExpect(jsonPath("naturalidade", equalTo("Naturalidade")))
                .andExpect(jsonPath("nacionalidade", equalTo("Nacionalidade")))
                .andExpect(jsonPath("links[0].rel", equalTo("self")))
                .andExpect(jsonPath("links[1].rel", equalTo("pessoas")))
                .andExpect(jsonPath("links[2].rel", equalTo("delete")));
    }

    @Test
    public void testInsert_apenasCamposObrigatorios() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO1();
        pessoaCadastroDTO.setNacionalidade(null);
        pessoaCadastroDTO.setNaturalidade(null);
        pessoaCadastroDTO.setEmail(null);
        pessoaCadastroDTO.setSexo(null);

        var requestBuilder = post(URI_API)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("nome", equalTo("Fulano Alberto")))
                .andExpect(jsonPath("cpf", equalTo("89231042050")))
                .andExpect(jsonPath("dataNascimento", equalTo("2000-01-02")))
                .andExpect(jsonPath("links[0].rel", equalTo("self")))
                .andExpect(jsonPath("links[1].rel", equalTo("pessoas")))
                .andExpect(jsonPath("links[2].rel", equalTo("delete")));
    }

    @Test
    public void testUpdate_pessoaNaoEncontrada() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setNome("Beltrano Alcides Atualizado");

        var requestBuilder = put(URI_API + "/0")
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdate_validateNomeNull() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setNome(null);

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.nome", any(String.class)));
    }

    @Test
    public void testUpdate_validateNomeEmpty() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setNome("");

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.nome", any(String.class)));
    }

    @Test
    public void testUpdate_validateNomeSize() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setNome("Fulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano "
                + "Fulano Fulano Fulano FulanoFulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano "
                + "Fulano Fulano Fulano Fulano FulanoFulano Fulano Fulano Fulano Fulano Fulano Fulano Fulano ");

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.nome", any(String.class)));
    }

    @Test
    public void testUpdate_validateCpfEmpty() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setCpf("");

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.cpf", any(String.class)));
    }

    @Test
    public void testUpdate_validateCpfInvalido() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setCpf("89231042052");

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.cpf", any(String.class)));
    }

    @Test
    public void tesUpdate_validateEmailInvalido() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setEmail("fulano");

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.email", any(String.class)));
    }

    @Test
    public void testUpdate_validateEmailSize() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setEmail(
                "fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano.fulano@gmail.com");

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.email", any(String.class)));
    }

    @Test
    public void tesUpdate_validateDataNascimentoNull() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setDataNascimento(null);

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.dataNascimento", any(String.class)));
    }

    @Test
    public void testUpdate_validateDataNascimentoFuturo() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setDataNascimento(LocalDate.now().plusDays(1));

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("messages.dataNascimento", any(String.class)));
    }

    @Test
    public void testUpdate_cpfDuplicado() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setCpf("06315862020");

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", any(String.class)));
    }

    @Test
    public void testUpdate_ok() throws Exception {

        var pessoaCadastroDTO = getPessoaCadastroDTO2();
        pessoaCadastroDTO.setNome("Beltrano Alcides Atualizado");

        var requestBuilder = put(URI_API + "/" + idPessoa2)
                .content(objectMapper.writeValueAsString(pessoaCadastroDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("nome", equalTo("Beltrano Alcides Atualizado")))
                .andExpect(jsonPath("cpf", equalTo("71954728093")))
                .andExpect(jsonPath("email", equalTo("beltrano@gmail.com")))
                .andExpect(jsonPath("sexo", equalTo("M")))
                .andExpect(jsonPath("dataNascimento", equalTo("1998-02-11")))
                .andExpect(jsonPath("naturalidade", equalTo("Naturalidade")))
                .andExpect(jsonPath("nacionalidade", equalTo("Nacionalidade")))
                .andExpect(jsonPath("links[0].rel", equalTo("self")))
                .andExpect(jsonPath("links[1].rel", equalTo("pessoas")))
                .andExpect(jsonPath("links[2].rel", equalTo("delete")));
    }

    @Test
    public void testDelete_pessoaNaoEncontrada() throws Exception {

        var requestBuilder = delete(URI_API + "/0")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDelete_ok() throws Exception {

        var requestBuilder = delete(URI_API + "/" + idPessoa2)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindById_pessoaNaoEncontrada() throws Exception {

        var requestBuilder = get(URI_API + "/0")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", any(String.class)));
    }

    @Test
    public void testFindById_ok() throws Exception {

        var requestBuilder = get(URI_API + "/" + idPessoa2)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("nome", equalTo("Beltrano Alcides")))
                .andExpect(jsonPath("cpf", equalTo("71954728093")))
                .andExpect(jsonPath("email", equalTo("beltrano@gmail.com")))
                .andExpect(jsonPath("sexo", equalTo("M")))
                .andExpect(jsonPath("dataNascimento", equalTo("1998-02-11")))
                .andExpect(jsonPath("naturalidade", equalTo("Naturalidade")))
                .andExpect(jsonPath("nacionalidade", equalTo("Nacionalidade")))
                .andExpect(jsonPath("links[0].rel", equalTo("self")))
                .andExpect(jsonPath("links[1].rel", equalTo("pessoas")))
                .andExpect(jsonPath("links[2].rel", equalTo("delete")));
    }

    @Test
    public void testFinAll_semSort() throws Exception {

        var requestBuilder = get(URI_API)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("links[0].rel", equalTo("self")))
                .andExpect(jsonPath("content.*", hasSize(2)));
    }

    @Test
    public void testFinAll_sortIdAsc() throws Exception {

        var requestBuilder = get(URI_API)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,asc")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("links[0].rel", equalTo("self")))
                .andExpect(jsonPath("content.*", hasSize(2)))
                .andExpect(jsonPath("content[0].id", equalTo(idPessoa2.intValue())));
    }

    @Test
    public void testFinAll_sortIdDesc() throws Exception {

        PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "id");

        var requestBuilder = get(URI_API)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,desc")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(httpBasic(USERNAME, PASSWORD));

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("links[0].rel", equalTo("self")))
                .andExpect(jsonPath("content.*", hasSize(2)))
                .andExpect(jsonPath("content[0].id", equalTo(idPessoa3.intValue())));
    }

    private PessoaCadastroDTO getPessoaCadastroDTO1() {

        return PessoaCadastroDTO.builder()
                .nome("Fulano Alberto")
                .cpf("89231042050")
                .email("fulano@gmail.com")
                .dataNascimento(LocalDate.of(2000, Month.JANUARY, 2))
                .nacionalidade("Nacionalidade")
                .naturalidade("Naturalidade")
                .sexo("M")
                .build();
    }

    private PessoaCadastroDTO getPessoaCadastroDTO2() {

        return PessoaCadastroDTO.builder()
                .nome("Beltrano Alcides")
                .cpf("71954728093")
                .email("beltrano@gmail.com")
                .dataNascimento(LocalDate.of(1998, Month.FEBRUARY, 11))
                .nacionalidade("Nacionalidade")
                .naturalidade("Naturalidade")
                .sexo("M")
                .build();
    }

    private PessoaCadastroDTO getPessoaCadastroDTO3() {

        return PessoaCadastroDTO.builder()
                .nome("Ciclano Fuentes")
                .cpf("06315862020")
                .email("ciclano@gmail.com")
                .dataNascimento(LocalDate.of(1998, Month.FEBRUARY, 11))
                .sexo("M")
                .build();
    }

    private Long setupInsert(PessoaCadastroDTO pessoaCadastroDTO) {

        var pessoa = modelMapper.map(pessoaCadastroDTO, Pessoa.class);

        pessoa = pessoaRepository.saveAndFlush(pessoa);

        return pessoa.getId();
    }

}