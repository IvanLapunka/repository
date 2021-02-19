package exceptions.service;

import all.repos.GroupRepository;
import all.repos.RepositoryFactory;
import all.repos.TeacherRepository;
import all.repos.TeacherRepositoryPostgres;
import exceptions.pojo.Group;
import exceptions.pojo.Teacher;

import java.util.Optional;
import java.util.Set;

public class TeacherServiceImpl implements TeacherService{
    GroupRepository groupRepository = RepositoryFactory.getGroupRepository();


    private TeacherServiceImpl() {

    }

    private static class TeacherServiceImplHolder {
        private static final TeacherServiceImpl INSTANCE_HOLDER = new TeacherServiceImpl();
    }

    public static TeacherServiceImpl getInstance() {
        return TeacherServiceImplHolder.INSTANCE_HOLDER;
    }

    TeacherRepository teacherRepository = TeacherRepositoryPostgres.getInstance();

    @Override
    public Set<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> getTeacher(Integer id) {
        return teacherRepository.find(id);
    }

    @Override
    public Optional<Teacher> getTeacherByLogin(String login) {
        return teacherRepository.findAll().stream()
                .filter(teacher -> teacher.getLogin().equals(login))
                .findAny();
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
        if (teacher == null) {
            return null;
        }
        if (teacher.getId() == null) {
            final Optional<Teacher> teacherByLogin = getTeacherByLogin(teacher.getLogin());
            Integer teacherId = teacherByLogin.orElse(new Teacher()).getId();
            teacher.setId(teacherId);
        }
        Group group = teacher.getGroup();
        if (group == null) {
            return teacherRepository.save(teacher);
        }
        Group existingGroup = groupRepository.findAll().stream()
                .filter(g -> g.getName().equals(group.getName()))
                .findAny()
                .orElseGet(() -> groupRepository.save(group.withTeacher(teacher)));
        return teacherRepository.save(teacher.withGroup(existingGroup));
    }

    @Override
    public Optional<Teacher> deleteTeacher(Integer id) {
        return teacherRepository.remove(id);
    }
}
