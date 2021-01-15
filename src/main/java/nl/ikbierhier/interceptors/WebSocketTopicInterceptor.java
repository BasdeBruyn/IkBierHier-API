package nl.ikbierhier.interceptors;

import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.User;
import nl.ikbierhier.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WebSocketTopicInterceptor implements ChannelInterceptor {

    @Autowired
    private AuthService authService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String destination = stompHeaderAccessor.getDestination();

        if (stompHeaderAccessor.getCommand().equals(StompCommand.SUBSCRIBE) && !this.userHasValidSubscription(destination))
            throw new MessagingException("No permission for this topic");
        else return message;
    }

    private boolean userHasValidSubscription(String destination) {
        boolean result = false;
        Optional<User> optionalUser = this.authService.getUser();

        if (optionalUser.isPresent() && destination != null) {
            User user = optionalUser.get();
            List<Group> groups = user.getGroups();

            String groupUuid = destination.substring(destination.length() - 36);

            List<Group> filteredGroup = groups.stream()
                    .filter(group -> groupUuid.equals(group.getUuid().toString()))
                    .collect(Collectors.toList());

            result = filteredGroup.size() > 0;
        }

        return result;
    }
}
