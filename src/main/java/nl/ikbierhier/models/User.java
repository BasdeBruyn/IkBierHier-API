package nl.ikbierhier.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "Users")
public class User {

    @Id
    private String id;

    @NotNull
    private String name;

    @JsonBackReference("user-locations")
    @OneToMany(mappedBy = "user")
    private List<Location> locations;

    @JsonBackReference("user-group")
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<Group> groups;

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Group> getGroups() {
        return groups;
    }
}
