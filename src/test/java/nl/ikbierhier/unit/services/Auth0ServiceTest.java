package nl.ikbierhier.unit.services;

import nl.ikbierhier.configs.TestSecurityConfig;
import nl.ikbierhier.models.UserProfile;
import nl.ikbierhier.services.Auth0Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class Auth0ServiceTest {

    @Mock
    public RestTemplate mockRestTemplate;

    @InjectMocks
    public Auth0Service auth0Service;

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        String response = "{" +
                "\"sub\": \"testuser\"," +
                "\"name\": \"testuser\"" +
                "}";

        when(mockRestTemplate.postForObject(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(response);
    }

    @Test
    public void test_getUserProfile_returnsUserObject() {
        TestSecurityConfig testSecurityConfig = new TestSecurityConfig();
        Jwt jwt = testSecurityConfig.jwt();

        assertThat(auth0Service.getUserProfile(jwt), instanceOf(UserProfile.class));
    }
}
