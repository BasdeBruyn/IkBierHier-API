package nl.ikbierhier.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.Message;
import nl.ikbierhier.models.User;
import nl.ikbierhier.repositories.GroupRepository;
import nl.ikbierhier.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MessageTest extends IntegrationTest {

    private User user;
    private Group group;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    public void addUserAndGroupToDatabase() {
        User user = new User("testuser", "Test user");
        Group group = new Group("Test group");

        this.user = this.userRepository.save(user);
        this.group = this.groupRepository.save(group);

        List<User> users = new ArrayList<User>();
        users.add(this.user);

        this.group.setAdmins(users);
        this.group.setUsers(users);

        this.groupRepository.save(this.group);
    }

    @Test
    public void test_POST_messageSend_statusOk() throws Exception {
        Date date = new Date();
        Timestamp createdAt = new Timestamp(date.getTime());
        Message message = new Message(this.group, this.user, "Hello World", createdAt);

        mockMvc.perform(post("/message/send").
                contentType("application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk());
    }
}
