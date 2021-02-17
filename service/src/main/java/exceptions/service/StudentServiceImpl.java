package exceptions.service;

import all.repos.GroupRepository;
import all.repos.RepositoryFactory;
import all.repos.StudentRepository;
import exceptions.pojo.Group;
import exceptions.pojo.Student;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class StudentServiceImpl implements StudentService {
    private static volatile StudentServiceImpl instance;
    private final StudentRepository studentRepository = RepositoryFactory.getStudentRepository();
    private final GroupRepository groupRepository = RepositoryFactory.getGroupRepository();

    private StudentServiceImpl() {}

    public static StudentServiceImpl getInstance() {
        if (instance == null) {
            synchronized (StudentServiceImpl.class) {
                if (instance == null) {
                    instance = new StudentServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Set<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudent(Integer id) {
        return studentRepository.find(id);
    }

    @Override
    public Student saveStudent(String login, String password, String firstName, String lastName, int age) {
        Student student = new Student()
                .withFirst_name(firstName)
                .withLast_name(lastName)
                .withLogin(login)
                .withPassword(password)
                .withAge(age);
        return (Student) studentRepository.save(student);
    }

    @Override
    public Optional<Student> getStudentByLogin(String login) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getLogin().equals(login))
                .findAny();
    }

    @Override
    public Student saveStudent(Student student) {
        if (student == null) {
            return null;
        }
        if (student.getId() == null) {
            final Optional<Student> studentByLogin = getStudentByLogin(student.getLogin());
            Integer studentId = studentByLogin.orElse(new Student()).getId();
            student.setId(studentId);
        }
        final Iterator<Group> iterator = student.getGroups().iterator();
        while (iterator.hasNext()) {
            Group next = iterator.next();
            if (next == null) continue;
            Group savedGroup = groupRepository.save(next);
            next.setId(savedGroup.getId());
        }
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> deleteStudent(Integer id) {
        return studentRepository.remove(id);
    }
}
