package nl.ikbierhier.unit.models;

import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.Invite;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class InviteTest {

    private static final long expiresAt = System.currentTimeMillis() / 1000L;
    private static Invite invite;

    @BeforeAll
    public static void initializeValues() {
        String group_uuid = "0";

        InviteTest.invite = new Invite(group_uuid, InviteTest.expiresAt);
    }

    @Test
    void test_getGroup_returnsNotNull() {
        assertThat(InviteTest.invite.getGroup_uuid(), notNullValue());
    }

    @Test
    void test_getExpiresAt_returnsGivenExpiresAt() {
        assertThat(InviteTest.invite.getExpiresAt(), is(System.currentTimeMillis() / 1000L));
    }
}
