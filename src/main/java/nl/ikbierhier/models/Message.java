package nl.ikbierhier.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "Messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "group_uuid", foreignKey = @ForeignKey(
            name = "MESSAGE_GROUP_UID_FK"
    ))
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(
            name = "MESSAGE_USER_ID_FK"
    ))
    private User user;

    @NotNull
    private String message;

    @NotNull
    private Timestamp createdAt;

    public Message() {}

    public Message(Group group, User user, String message, Timestamp createdAt) {
        this.user = user;
        this.group = group;
        this.message = message;
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Group getGroup() {
        return group;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
