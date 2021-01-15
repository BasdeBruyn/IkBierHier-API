package nl.ikbierhier.unit.models;

import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.Location;
import nl.ikbierhier.models.Message;
import nl.ikbierhier.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupTest {

    private static final String name = "Group name";
    private static final String description = "Some description";

    private static List<User> users;
    private static List<User> admins;
    private static List<Message> messages;
    private static List<Location> locations;

    private Group group;

    @BeforeAll
    public static void initializeValues() {
        User user = new User("facebook|some_id", "Henk Renk");

        GroupTest.users = new ArrayList<>();
        GroupTest.users.add(user);

        GroupTest.admins = new ArrayList<>();
        GroupTest.admins.add(user);

        Message message = new Message();

        GroupTest.messages = new ArrayList<>();
        GroupTest.messages.add(message);

        Location location = new Location();

        GroupTest.locations = new ArrayList<>();
        GroupTest.locations.add(location);
    }

    @BeforeEach
    public void initializeGroup() {
        this.group = new Group(
                GroupTest.name,
                GroupTest.description,
                GroupTest.users,
                GroupTest.admins,
                GroupTest.messages,
                GroupTest.locations
        );
    }

    @Test
    void test_getName_returnsGivenname() {
        assertThat(this.group.getName(), is(GroupTest.name));
    }

    @Test
    void test_getDescription_returnsGivenDescription() {
        assertThat(this.group.getDescription(), is(GroupTest.description));
    }

    @Test
    void test_getUsers_returnsGivenUsers() {
        assertThat(this.group.getUsers().size(), is(GroupTest.users.size()));
    }

    @Test
    void test_setUsers_returnsDefinedUsers() {
        List<User> newUsers = new ArrayList<>();

        this.group.setUsers(newUsers);

        assertThat(this.group.getUsers().size(), is(newUsers.size()));
    }

    @Test
    void test_getAdmins_returnsGivenAdmins() {
        assertThat(this.group.getAdmins().size(), is(GroupTest.admins.size()));
    }

    @Test
    void test_setAdmins_returnsDefinedAdmins() {
        List<User> newAdmins = new ArrayList<>();

        this.group.setAdmins(newAdmins);

        assertThat(this.group.getAdmins().size(), is(newAdmins.size()));
    }

    @Test
    void test_getMessages_returnsGivenMessages() {
        assertThat(this.group.getMessages().size(), is(GroupTest.messages.size()));
    }

    @Test
    void test_getLocations_returnsGivenLocations() {
        assertThat(this.group.getLocations().size(), is(GroupTest.locations.size()));
    }
}
