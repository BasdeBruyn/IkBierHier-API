package nl.ikbierhier.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

@Entity(name = "Locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "group_uuid", foreignKey = @ForeignKey(
            name = "LOCATION_GROUP_ID_FK"
    ))
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(
            name = "LOCATION_USER_ID_FK"
    ))
    private User user;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private Timestamp expiresAt;

    public Location() {}
    public Location(Group group, User user, double latitude, double longitude, Timestamp expiresAt) {
        this.group = group;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.expiresAt = expiresAt;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        Calendar calendar = Calendar.getInstance();
        Timestamp now = new Timestamp(calendar.getTime().getTime());

        return expiresAt.before(now);
    }
}
