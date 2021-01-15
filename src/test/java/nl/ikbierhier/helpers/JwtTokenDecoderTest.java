package nl.ikbierhier.helpers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(classes = JwtTokenDecoder.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class JwtTokenDecoderTest {

    @Autowired
    JwtTokenDecoder jwtTokenDecoder;

    @Test
    public void test_getJwtDecoder_ShouldReturnJwtDecoder() {
        JwtDecoder jwtDecoder = jwtTokenDecoder.getJwtDecoder();

        assertThat(jwtDecoder, notNullValue());
    }
}
