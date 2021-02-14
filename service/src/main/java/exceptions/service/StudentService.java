package exceptions.service;

import exceptions.pojo.Student;

import java.util.Optional;
import java.util.Set;

public interface StudentService {
    Set<Student> getAllStudents();
    Optional<Student> getStudent(Integer id);
    Student saveStudent(String login, String password, String firstName, String lastName, int age);
    Student saveStudent(Student student);
    Optional<Student> deleteStudent(Integer id);
}
