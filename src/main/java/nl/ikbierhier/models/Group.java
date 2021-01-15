package nl.ikbierhier.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity(name = "Groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @NotNull
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<User> users;

    @JsonBackReference("group-admins")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> admins;

    @JsonBackReference("group-messages")
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Message> messages;

    @JsonBackReference("group-location")
    @OneToMany(mappedBy = "group")
    private List<Location> locations;

    public Group() {}

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, String description, List<User> users, List<User> admins) {
        this.name = name;
        this.description = description;
        this.users = users;
        this.admins = admins;
    }

    public Group(String name, String description, List<User> users, List<User> admins, List<Message> messages, List<Location> locations
    ) {
        this.name = name;
        this.description = description;
        this.users = users;
        this.admins = admins;
        this.messages = messages;
        this.locations = locations;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
