package nl.ikbierhier.repositories;

import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.Location;
import nl.ikbierhier.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    void deleteByUserAndGroup(User user, Group group);
    List<Location> findAllByGroupInAndExpiresAtAfter(List<Group> groups, Timestamp timestamp);
}
