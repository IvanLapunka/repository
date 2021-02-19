package exceptions.service;

import all.repos.GroupRepository;
import all.repos.RepositoryFactory;
import all.repos.StudentRepository;
import all.repos.TeacherRepository;
import exceptions.pojo.Group;
import exceptions.pojo.Student;
import exceptions.pojo.Teacher;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class GroupServiceImpl implements GroupService{
    GroupRepository groupRepository = RepositoryFactory.getGroupRepository();
    TeacherRepository teacherRepository = RepositoryFactory.getTeacherRepository();

    private GroupServiceImpl() {

    }

    private static class GroupServiceHolder {
        private static final GroupServiceImpl INSTANCE_HOLDER = new GroupServiceImpl();
    }

    public static GroupServiceImpl getInstance() {
        return GroupServiceHolder.INSTANCE_HOLDER;
    }

    @Override
    public Optional<Group> getGroup(Integer id) {
        return groupRepository.find(id);
    }

    @Override
    public Set<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupByName(String groupName) {
        return groupRepository.findAll().stream()
                .filter(group -> group.getName().equals(groupName))
                .findAny();
    }

    @Override
    public Group saveGroup(Group group) {
        if (group == null) {
            return null;
        }
        final Optional<Group> groupByName;
        if (group.getId() == null) {
            groupByName = getGroupByName(group.getName());
            Integer groupId = groupByName.orElse(new Group()).getId();
            group.setId(groupId);
        }
        Teacher teacher = group.getTeacher();
        if (teacher == null) {
            return groupRepository.save(group);
        }
        Teacher teacher1 = teacherRepository.findAll().stream()
                .filter(t -> t.getLogin().equals(teacher.getLogin()))
                .findAny()
                .orElseGet(() -> teacherRepository.save(teacher.withGroup(group)));
        return groupRepository.save(group.withTeacher(teacher1));
    }

    @Override
    public Optional<Group> deleteGroup(Integer id) {
        return groupRepository.remove(id);
    }
}
