package nl.ikbierhier.controllers;

import nl.ikbierhier.models.Group;
import nl.ikbierhier.models.User;
import nl.ikbierhier.services.GroupService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/group")
@RestController
public class GroupController {

    private GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }


    @PostMapping
    public Group postGroup(@Valid @RequestBody Group group) {
        service.postGroup(group);
        return group;
    }

    @PostMapping("/add/{groupId}")
    public void addUser(@Valid @RequestBody User user, @PathVariable UUID groupId) {
        Group group = service.getGroup(groupId);
        List<User> users = group.getUsers();

//        group.getUsers().stream().anyMatch(u -> user.getId().equals(u.getId()));

        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getId().equals(user.getId())){
                return;
            }
        }

        users.add(user);
        group.setUsers(users);
        service.postGroup(group);
    }

    @GetMapping("/{groupId}")
    public Group getGroup(@PathVariable UUID groupId) {
        return service.getGroup(groupId);
    }

    @GetMapping
    public Iterable<Group> getAllGroups() {
        return service.getAllGroupsForUser();
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable UUID groupId) {
        service.deleteGroup(groupId);
    }

}