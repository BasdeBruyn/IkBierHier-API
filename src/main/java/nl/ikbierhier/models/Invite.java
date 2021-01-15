package nl.ikbierhier.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity(name = "Invites")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String group_uuid;

    @NotNull
    private long expiresAt;

    public Invite() {}

    public Invite(String group_uuid, long expiresAt) {
        this.group_uuid = group_uuid;
        this.expiresAt = expiresAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getGroup_uuid() {
        return group_uuid;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setGroup_uuid(String group_uuid) {
        this.group_uuid = group_uuid;
    }

    public void setExpiresAt(long timestamp){
        this.expiresAt = timestamp;
    }
}
