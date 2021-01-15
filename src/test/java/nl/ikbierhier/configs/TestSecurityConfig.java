package nl.ikbierhier.configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestConfiguration
public class TestSecurityConfig {
    public static final String TOKEN = "token";
    public static final String SUB = "sub";
    public static final String ID = "facebook|12345678";
    public static final String AUDIENCE = "aud";

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            @Override
            public Jwt decode(String s) throws JwtException {
                return jwt();
            }
        };
    }

    public Jwt jwt() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "none");

        Map<String, Object> claims = new HashMap<>();
        claims.put(SUB, ID);

        List<String> audience = new ArrayList<>();
        audience.add(AUDIENCE);

        claims.put(JwtClaimNames.AUD, audience);

        return new Jwt(
                TOKEN,
                Instant.now(),
                Instant.now().plusSeconds(30),
                headers, claims
        );
    }

}
