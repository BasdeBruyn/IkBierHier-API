package nl.ikbierhier.services;

import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.User;
import nl.ikbierhier.repositories.GroupRepository;
import nl.ikbierhier.statusErrors.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final AuthService authService;

    public GroupService(GroupRepository groupRepository, AuthService authService) {
        this.groupRepository = groupRepository;
        this.authService = authService;
    }

    public void postGroup(Group group) {
        groupRepository.save(group);
    }

    public Group getGroup(UUID groupId) {
        return groupRepository.findById(groupId).orElse(null);
    }

    public List<Group> getAllGroupsForUser() {
        List<User> user = Collections.singletonList(authService.getUser().orElseThrow(NotFoundException::new));
        return groupRepository.findAllByUsersIn(user);
    }

    public void deleteGroup(UUID groupId) {
        groupRepository.deleteById(groupId);
    }

}