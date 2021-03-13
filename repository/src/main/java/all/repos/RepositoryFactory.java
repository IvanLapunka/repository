package all.repos;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;
@Slf4j
public class RepositoryFactory {
    private static RepositoryType type;

    static {
        Properties dbProperties = new Properties();
        try {
            dbProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        type = RepositoryType.getRepositoryTypeByString(dbProperties.getProperty("repository.type"));
    }

    public static StudentRepository getStudentRepository() {
        StudentRepository studentRepository;
        if (type == RepositoryType.POSTGRES) {
            studentRepository = StudentRepositoryPostgres.getInstance();
        } else if (type == RepositoryType.JPA) {
            studentRepository = StudentRepositoryJpa.getInstance();
        } else {
            studentRepository = StudentRepositoryInMemory.getInstance();
        }
        return studentRepository;
    }

    public static TeacherRepository getTeacherRepository() {
        TeacherRepository teacherRepository;
        if (type == RepositoryType.POSTGRES) {
            teacherRepository = TeacherRepositoryPostgres.getInstance();
        } else if (type == RepositoryType.JPA) {
            teacherRepository = TeacherRepositoryJpa.getInstance();
        } else {
            teacherRepository = TeacherRepositoryInMemory.getInstance();
        }
        return teacherRepository;
    }

    public static GroupRepository getGroupRepository() {
        GroupRepository groupRepository;
        if (type == RepositoryType.POSTGRES) {
            groupRepository = GroupRepositoryPostgres.getInstance();
        } else if (type == RepositoryType.JPA) {
            groupRepository = GroupRepositoryJpa.getInstance();
        } else {
            groupRepository = GroupRepositoryInMemory.getInstance();
        }
        return groupRepository;
    }
}
