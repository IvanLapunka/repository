package exceptions.service;

import exceptions.pojo.Student;
import exceptions.pojo.Teacher;

import java.util.Optional;
import java.util.Set;

public interface TeacherService {
    Set<Teacher> getAllTeachers();
    Optional<Teacher> getTeacher(Integer id);
    Teacher saveTeacher(String login, String password, String firstName, String lastName, int age);
    Teacher saveTeacher(Teacher teacher);
}