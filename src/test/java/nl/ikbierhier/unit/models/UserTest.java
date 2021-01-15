package nl.ikbierhier.unit.models;

import nl.ikbierhier.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserTest {

    private static final String id = "some_id";
    private static final String name = "Henk renk";
    private static User user;

    @BeforeAll
    public static void initializeValues() {
        UserTest.user = new User(UserTest.id, UserTest.name);
    }

    @Test
    void test_getId_returnsGivenId() {
        assertThat(UserTest.user.getId(), is(UserTest.id));
    }

    @Test
    void test_getName_returnsGivenName() {
        assertThat(UserTest.user.getName(), is(UserTest.name));
    }
}
