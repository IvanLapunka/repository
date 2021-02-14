package exceptions.service;

import all.repos.GroupRepository;
import all.repos.GroupRepositoryInMemory;
import all.repos.TeacherRepository;
import all.repos.TeacherRepositoryInMemory;
import exceptions.pojo.Group;
import exceptions.pojo.Student;
import exceptions.pojo.Teacher;

import java.util.Optional;
import java.util.Set;

public class TeacherServiceImpl implements TeacherService{
    GroupRepository groupRepository = GroupRepositoryInMemory.getInstance();


    private TeacherServiceImpl() {

    }

    private static class TeacherServiceImplHolder {
        private static final TeacherServiceImpl INSTANCE_HOLDER = new TeacherServiceImpl();
    }

    public static TeacherServiceImpl getInstance() {
        return TeacherServiceImplHolder.INSTANCE_HOLDER;
    }

    TeacherRepository teacherRepository = TeacherRepositoryInMemory.getInstance();

    @Override
    public Set<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> getTeacher(Integer id) {
        return teacherRepository.find(id);
    }

    @Override
    public Teacher saveTeacher(String login, String password, String firstName, String lastName, int age) {
        return teacherRepository.save(new Teacher()
                .withLogin(login)
                .withPassword(password)
                .withFirst_name(firstName)
                .withLast_name(lastName)
                .withAge(age));
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        Group group = teacher.getGroup();
        if (group == null) {
            teacherRepository.save(teacher);
        }
        Group existingGroup = groupRepository.findAll().stream()
                .filter(g -> g.getName().equals(group.getName()))
                .findAny()
                .orElseGet(() -> groupRepository.save(group.withTeacher(teacher)));
        return teacherRepository.save(teacher.withGroup(existingGroup));
    }
}
