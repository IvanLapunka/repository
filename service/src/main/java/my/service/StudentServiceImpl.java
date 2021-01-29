package my.service;

import my.pojo.Student;
import my.repos.Repository;
import my.repos.StudentRepository;
import my.repos.StudentRepositoryInMemory;

import java.util.Set;

public class StudentServiceImpl implements StudentService {
    private static volatile StudentServiceImpl instance;
    private final Repository repository = StudentRepositoryInMemory.getInstance();

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
        return repository.findAll();
    }

    @Override
    public Student saveStudent(String login, String password, String firstName, String lastName, int age) {
        Student student = new Student()
                .withFirst_name(firstName)
                .withLast_name(lastName)
                .withLogin(login)
                .withPassword(password)
                .withAge(age);
        return (Student) repository.save(student);
    }
}
