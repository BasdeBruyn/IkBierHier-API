package nl.ikbierhier.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.List;

public class TestGroup {

    private static final String name = "Group name";
    private static final String description = "Some description";

    private static List<User> users;
    private static List<User> admins;
    private static List<Message> messages;
    private static List<Location> locations;
    private static List<Invite> invites;

    private Group group;

    @BeforeAll
    public static void initializeValues() {
        User user = new User("facebook|some_id", "Henk Renk");

        TestGroup.users = new ArrayList<>();
        TestGroup.users.add(user);

        TestGroup.admins = new ArrayList<>();
        TestGroup.admins.add(user);

        Message message = new Message();

        TestGroup.messages = new ArrayList<>();
        TestGroup.messages.add(message);

        Location location = new Location();

        TestGroup.locations = new ArrayList<>();
        TestGroup.locations.add(location);

        Invite invite = new Invite();

        TestGroup.invites = new ArrayList<>();
        TestGroup.invites.add(invite);
    }

    @BeforeEach
    public void initializeGroup() {
        this.group = new Group(
                TestGroup.name,
                TestGroup.description,
                TestGroup.users,
                TestGroup.admins,
                TestGroup.messages,
                TestGroup.locations
//                TestGroup.invites
        );
    }

    @Test
    void getName() {
        assertThat(this.group.getName(), is(TestGroup.name));
    }

    @Test
    void getDescription() {
        assertThat(this.group.getDescription(), is(TestGroup.description));
    }

    @Test
    void getUsers() {
        assertThat(this.group.getUsers().size(), is(TestGroup.users.size()));
    }

    @Test
    void setUsers() {
        List<User> newUsers = new ArrayList<>();

        this.group.setUsers(newUsers);

        assertThat(this.group.getUsers().size(), is(newUsers.size()));
    }

    @Test
    void getAdmins() {
        assertThat(this.group.getAdmins().size(), is(TestGroup.admins.size()));
    }

    @Test
    void setAdmins() {
        List<User> newAdmins = new ArrayList<>();

        this.group.setAdmins(newAdmins);

        assertThat(this.group.getAdmins().size(), is(newAdmins.size()));
    }

    @Test
    void getMessages() {
        assertThat(this.group.getMessages().size(), is(TestGroup.messages.size()));
    }

    @Test
    void getLocations() {
        assertThat(this.group.getLocations().size(), is(TestGroup.locations.size()));
    }

//    @Test
//    void getInvites() {
//        assertThat(this.group.getInvites().size(), is(TestGroup.invites.size()));
//    }
}
