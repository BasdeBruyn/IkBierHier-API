package nl.ikbierhier.interceptors;

import nl.ikbierhier.configs.TestSecurityConfig;
import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.Message;
import nl.ikbierhier.models.User;
import nl.ikbierhier.repositories.GroupRepository;
import nl.ikbierhier.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
@ContextConfiguration(classes = TestSecurityConfig.class)
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WebSocketSecurityInterceptorTest {

    @Value("${local.server.port}")
    private int port;

    static String WEBSOCKET_URI;
    static final String WEBSOCKET_TOPIC = "/topic";

    private WebSocketStompClient webSocketStompClient;

    private User user;
    private Group group;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    public void addUserAndGroupToDatabase() {
        User user = new User(TestSecurityConfig.ID, "Test user");
        Group group = new Group("Test group");

        this.user = this.userRepository.save(user);
        this.group = this.groupRepository.save(group);

        List<User> users = new ArrayList<User>();
        users.add(this.user);

        this.group.setAdmins(users);
        this.group.setUsers(users);

        this.groupRepository.save(this.group);
    }

    @BeforeEach
    public void setupWebsocket() {
        WEBSOCKET_URI = "http://localhost:" + port + "/api/websocket";

        this.webSocketStompClient = new WebSocketStompClient(
                new SockJsClient(
                        Arrays.asList(new WebSocketTransport(
                                new StandardWebSocketClient()
                        ))
                )
        );

        this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void test_websocketWithUnauthoziredUser_shouldNotReceiveMessageFromTheServer() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            CompletableFuture<Message> completableFuture = new CompletableFuture();

            StompSession stompSession = webSocketStompClient
                    .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {
                    }).get(1, TimeUnit.SECONDS);
            stompSession.subscribe(WEBSOCKET_TOPIC + "/" + group.getUuid(), new DefaultStompFrameHandler(completableFuture));

            sendMessage();

            completableFuture.get(5, TimeUnit.SECONDS);
        });
    }

    private void sendMessage() {
        Date date = new Date();
        Timestamp createdAt = new Timestamp(date.getTime());
        Message message = new Message(this.group, this.user, "Hello World", createdAt);

        this.simpMessagingTemplate.convertAndSend(WEBSOCKET_TOPIC + "/" + this.group.getUuid(), message);
    }

    class DefaultStompFrameHandler implements StompFrameHandler {

        private CompletableFuture completableFuture;

        public DefaultStompFrameHandler(CompletableFuture completableFuture) {
            this.completableFuture = completableFuture;
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return Message.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((Message) o);
        }
    }
}
