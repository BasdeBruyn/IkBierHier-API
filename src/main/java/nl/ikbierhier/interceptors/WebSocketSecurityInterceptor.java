package nl.ikbierhier.interceptors;

import nl.ikbierhier.helpers.JwtTokenDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtTokenDecoder jwtTokenDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        List<String> header = stompHeaderAccessor.getNativeHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            String token = header.get(0).split(" ")[1];

            JwtDecoder jwtDecoder = this.jwtTokenDecoder.getJwtDecoder();
            Jwt jwt = jwtDecoder.decode(token);
            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
            Authentication authentication = jwtAuthenticationConverter.convert(jwt);

            stompHeaderAccessor.setUser(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return message;
    }


}
