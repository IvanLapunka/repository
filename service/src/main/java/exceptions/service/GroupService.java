package exceptions.service;

import exceptions.pojo.Group;
import exceptions.pojo.Teacher;

import java.util.Optional;
import java.util.Set;

public interface GroupService {
    Optional<Group> getGroup(Integer id);
    Set<Group> getAllGroups();
    Group saveGroup(Group group);
    Optional<Group> deleteGroup(Integer id);
}
