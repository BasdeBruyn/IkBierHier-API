package nl.ikbierhier.services;

import nl.ikbierhier.interfaces.Auth0UserProfileClaims;
import nl.ikbierhier.models.UserProfile;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Auth0Service {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUrl;

    @Autowired
    public RestTemplate restTemplate;

    public UserProfile getUserProfile(Jwt jwt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt.getTokenValue());

        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
        String response = restTemplate.postForObject(this.issuerUrl + "/userinfo", httpEntity, String.class);

        JSONObject jsonObject = new JSONObject(response);

        return new UserProfile(
                jsonObject.getString(Auth0UserProfileClaims.SUB),
                jsonObject.getString(Auth0UserProfileClaims.NAME)
        );
    }
}