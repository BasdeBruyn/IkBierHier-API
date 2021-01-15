package nl.ikbierhier.repositories;

import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends CrudRepository<Group, UUID> {
    List<Group> findAllByUsersIn(List<User> users);
}
