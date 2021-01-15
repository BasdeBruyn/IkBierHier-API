package nl.ikbierhier.unit.models;

import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.Message;
import nl.ikbierhier.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MessageTest {
    private static final String stringMessage = "Hello world";
    private static final Timestamp createdAt = new Timestamp(Calendar.getInstance().getTime().getTime());
    private static Message message;

    @BeforeAll
    public static void initializeValues() {
        Group group = new Group();
        User user = new User();

        MessageTest.message = new Message(group, user, MessageTest.stringMessage, MessageTest.createdAt);
    }

    @Test
    void test_getGroup_returnsNotnull() {
        assertThat(MessageTest.message.getGroup(), notNullValue());
    }

    @Test
    void test_getUser_returnsNotNull() {
        assertThat(MessageTest.message.getUser(), notNullValue());
    }

    @Test
    void test_getMessage_returnsGivenMessage() {
        assertThat(MessageTest.message.getMessage(), is(MessageTest.stringMessage));
    }

    @Test
    void test_getCreatedAt_returnsGivenCreatedAt() {
        assertThat(MessageTest.message.getCreatedAt().getTime(), is(MessageTest.createdAt.getTime()));
    }
}
