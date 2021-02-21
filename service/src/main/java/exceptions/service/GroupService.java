package exceptions.service;

import by.pojo.Group;

import java.util.Optional;
import java.util.Set;

public interface GroupService {
    Optional<Group> getGroup(Integer id);
    Optional<Group> getGroupByName(String groupName);
    Set<Group> getAllGroups();
    Group saveGroup(Group group);
    Optional<Group> deleteGroup(Integer id);
}
