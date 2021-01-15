package nl.ikbierhier.validators;

import nl.ikbierhier.configs.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AudienceValidatorTest {

    @Test
    public void test_ValidateWithValidAudience_returnsOAuth2TokenValidatorResult_hasNoErrors() {
        TestSecurityConfig testSecurityConfig = new TestSecurityConfig();
        AudienceValidator audienceValidator = new AudienceValidator(TestSecurityConfig.AUDIENCE);

        OAuth2TokenValidatorResult result = audienceValidator.validate(testSecurityConfig.jwt());

        assertThat(result.hasErrors(), is(false));
    }

    @Test
    public void test_ValidateWithInvalidAudience_returnsOAuth2TokenValidatorResult_hasNoErrors() {
        TestSecurityConfig testSecurityConfig = new TestSecurityConfig();
        AudienceValidator audienceValidator = new AudienceValidator("invalid_audience");

        OAuth2TokenValidatorResult result = audienceValidator.validate(testSecurityConfig.jwt());

        assertThat(result.hasErrors(), is(true));
    }
}
