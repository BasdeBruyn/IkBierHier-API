package nl.ikbierhier.models;

import nl.ikbierhier.unit.models.InviteTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Timestamp;
import java.util.Calendar;

public class TestInvite {

    private static final Long expiresAt = System.currentTimeMillis() / 1000L;
    private static Invite invite;

    @BeforeAll
    public static void initializeValues() {
        String group_uuid = "0";

        TestInvite.invite = new Invite(group_uuid, TestInvite.expiresAt);
    }

    @Test
    void test_getGroup_returnsNotNull() {
        assertThat(TestInvite.invite.getGroup_uuid(), notNullValue());
    }

    @Test
    void test_getExpiresAt_returnsGivenExpiresAt() {
        assertThat(TestInvite.invite.getExpiresAt(), is(System.currentTimeMillis() / 1000L));
    }
}
