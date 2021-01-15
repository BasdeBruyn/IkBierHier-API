package nl.ikbierhier.services;

import nl.ikbierhier.models.User;
import nl.ikbierhier.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> getUser() {
        Jwt jwt = this.getJwt();

        String id = jwt.getClaim(JwtClaimNames.SUB);
        return userRepository.findById(id);
    }

    public Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object credentials = authentication.getCredentials();

        if (credentials instanceof Jwt) return (Jwt) credentials;
        return null;
    }
}
