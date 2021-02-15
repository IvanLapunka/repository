package exceptions.service;

import all.repos.GroupRepository;
import all.repos.GroupRepositoryInMemory;
import all.repos.GroupRepositoryPostgres;
import all.repos.RepositoryFactory;
import all.repos.TeacherRepository;
import all.repos.TeacherRepositoryInMemory;
import all.repos.TeacherRepositoryPostgres;
import exceptions.pojo.Group;
import exceptions.pojo.Teacher;

import java.util.Optional;
import java.util.Set;

public class GroupServiceImpl implements GroupService{
    GroupRepository groupRepository = RepositoryFactory.getGroupRepository();
    TeacherRepository teacherRepository = RepositoryFactory.getTeacherRepository();

    private GroupServiceImpl () {

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

    @Override
    public Group saveGroup(Group group) {
        Teacher teacher = group.getTeacher();
        if (teacher == null) {
            return groupRepository.save(group);
        }
        teacherRepository.findAll().stream()
                .filter(t -> t.getLogin().equals(teacher.getLogin()))
                .findAny()
                .orElseGet(() -> teacherRepository.save(teacher.withGroup(group)));
        return groupRepository.save(group.withTeacher(teacher));
    }

    @Override
    public Optional<Group> deleteGroup(Integer id) {
        return groupRepository.remove(id);
    }
}
