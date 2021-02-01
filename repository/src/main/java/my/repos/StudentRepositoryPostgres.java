package my.repos;

import lombok.extern.slf4j.Slf4j;
import my.pojo.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//@Slf4j
public class StudentRepositoryPostgres implements Repository<Student>{
    private final Datasource datasource = Datasource.getInstance();
    private static final String GET_ALL_STUDENTS = " SELECT id, login, \"password\", first_name, last_name, age FROM model3.student";
    private static final String GET_STUDENT_BY_ID = GET_ALL_STUDENTS + " where id = ?";

    private static class StudentRepositoryPostgresHolder {
        private final static StudentRepositoryPostgres HOLDER_INSTANCE = new StudentRepositoryPostgres();
    }

    public static StudentRepositoryPostgres getInstance() {
        return StudentRepositoryPostgresHolder.HOLDER_INSTANCE;
    }

    @Override
    public Set<Student> findAll() {
        Set<Student> students = new HashSet<>();
        try(Connection conn = datasource.getConnection();
            PreparedStatement stat = conn.prepareStatement(GET_ALL_STUDENTS);
            ResultSet res = stat.executeQuery();)
        {
            while (res.next()) {
                Student student = new Student()
                        .withAge(res.getInt("age"))
                        .withFirst_name(res.getString("first_name"))
                        .withLast_name(res.getString("last_name"))
                        .withLogin(res.getString("login"))
                        .withPassword(res.getString("password"))
                        .withId(res.getInt("id"));
                students.add(student);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public Optional<Student> find(Integer id) {
        Student student = null;
        try (Connection conn = datasource.getConnection();
             PreparedStatement pStat = conn.prepareStatement(GET_STUDENT_BY_ID);
        )   {
            pStat.setInt(1, id);
            ResultSet rs = pStat.executeQuery();
            if (rs.next()) {
                rs.getRow();
                student = new Student()
                        .withAge(rs.getInt("age"))
                        .withLogin(rs.getString("login"))
                        .withPassword(rs.getString("password"))
                        .withFirst_name(rs.getString("first_name"))
                        .withLast_name(rs.getString("last_name"))
                        .withId(rs.getInt("id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(student);
    }

    @Override
    public Student save(Student entity) {
        return null;
    }

    @Override
    public Optional<Student> remove(Integer id) {
        return Optional.empty();
    }
}
