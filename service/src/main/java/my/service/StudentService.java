package my.service;

import my.pojo.Student;

import java.util.Set;

public interface StudentService {
    Set<Student> getAllStudents();
    Student saveStudent(String login, String password, String firstName, String lastName, int age);
}
