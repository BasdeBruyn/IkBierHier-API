package nl.ikbierhier.unit.services;

import nl.ikbierhier.configs.TestSecurityConfig;
import nl.ikbierhier.models.User;
import nl.ikbierhier.repositories.UserRepository;
import nl.ikbierhier.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {TestSecurityConfig.class})
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthServiceTest {

    private TestSecurityConfig testSecurityConfig = new TestSecurityConfig();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    private Authentication mockAuthentication = mock(Authentication.class);
    private SecurityContext mockSecurityContext = mock(SecurityContext.class);

    @BeforeEach
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        SecurityContextHolder.setContext(mockSecurityContext);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getCredentials()).thenReturn(testSecurityConfig.jwt());
    }

    @BeforeEach
    public void storeUserInDatabase() {
        User user = new User(TestSecurityConfig.ID, "Henk");
        this.userRepository.save(user);
    }

    @Test
    public void test_getUser_returnsUserObject() {
        when(mockAuthentication.getCredentials()).thenReturn(testSecurityConfig.jwt());

        Optional<User> user = this.authService.getUser();
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void test_getJwt_returnsJwtObject() {
        when(mockAuthentication.getCredentials()).thenReturn(testSecurityConfig.jwt());

        Jwt jwt = this.authService.getJwt();
        assertThat(jwt, instanceOf(Jwt.class));
    }

    @Test
    public void test_getJwt_returnsNull() {
        when(mockAuthentication.getCredentials()).thenReturn(null);

        Jwt jwt = this.authService.getJwt();
        assertThat(jwt, nullValue());
    }
}
