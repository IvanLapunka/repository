package exceptions.service;

import exceptions.pojo.Student;
import all.repos.Repository;
import all.repos.StudentRepositoryPostgres;

import java.util.Optional;
import java.util.Set;

public class StudentServiceImpl implements StudentService {
    private static volatile StudentServiceImpl instance;
    private final Repository repository = StudentRepositoryPostgres.getInstance(); //StudentRepositoryInMemory.getInstance();

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
    public Optional<Student> getStudent(Integer id) {
        return repository.find(id);
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

    @Override
    public Student saveStudent(Student student) {
        return (Student) repository.save(student);
    }
}
