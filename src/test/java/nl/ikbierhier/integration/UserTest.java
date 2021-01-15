package nl.ikbierhier.integration;

import nl.ikbierhier.configs.TestSecurityConfig;
import nl.ikbierhier.models.User;
import nl.ikbierhier.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserTest extends IntegrationTest {

    @MockBean
    private RestTemplate mockRestTemplate;

    @Autowired
    private UserRepository userRepository;

    public final String USER_ID = "testuser";
    public final String USER_NAME = "testuser";

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        String response = "{" +
                "\"sub\": " + USER_ID + "," +
                "\"name\": " + USER_NAME +
                "}";

        when(mockRestTemplate.postForObject(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(response);
    }

    @Test
    public void test_POST_registerUser_statusForbidden() throws Exception {
        User user = new User("id", "name");

        mockMvc.perform(
                post("/user/signup")
        ).andExpect(status().isForbidden());
    }

    @Test
    public void test_POST_registerUser_statusCreated() throws Exception {
        mockMvc.perform(
                post("/user/signup")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
        ).andExpect(status().isCreated());
    }

    @Test
    public void test_POST_registerUser_statusOk() throws Exception {
        User user = new User(TestSecurityConfig.ID, USER_NAME);
        this.userRepository.save(user);

        mockMvc.perform(
                post("/user/signup")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
        ).andExpect(status().isOk());
    }

    @Test
    public void test_GET_getUser_statusOk() throws Exception {
        User user = new User(TestSecurityConfig.ID, USER_NAME);
        this.userRepository.save(user);

        mockMvc.perform(
                get("/user/get")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
        ).andExpect(status().isOk());
    }

    @Test
    public void test_GET_getUser_statusNotFound() throws Exception {
        mockMvc.perform(
                get("/user/get")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
        ).andExpect(status().isNotFound());
    }
}
