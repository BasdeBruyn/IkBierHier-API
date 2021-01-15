package nl.ikbierhier.controllers;

import nl.ikbierhier.models.User;
import nl.ikbierhier.models.UserProfile;
import nl.ikbierhier.repositories.UserRepository;
import nl.ikbierhier.services.Auth0Service;
import nl.ikbierhier.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private Auth0Service auth0Service;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity register() {
        Optional<User> user = this.authService.getUser();

        if (!user.isPresent()) {
            UserProfile userProfile = this.auth0Service.getUserProfile(
                    this.authService.getJwt()
            );

            User newUser = new User(userProfile.sub, userProfile.name);
            this.userRepository.save(newUser);

            return new ResponseEntity(null, HttpStatus.CREATED);
        }

        return new ResponseEntity(null, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<User> getUser() {
        Optional<User> user = this.authService.getUser();

        return user.map(value -> new ResponseEntity(value, null, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity(null, HttpStatus.NOT_FOUND));
    }

//    @GetMapping("/groups/{userID}")
//    public Group getGroup(@PathVariable UUID userId) {
//        User user = new User();
//        user = this.getUser();
//        return this.getUser();
//    }
}