package all.repos;

import exceptions.DatabaseException;
import exceptions.pojo.Group;
import exceptions.pojo.Student;
import exceptions.pojo.Teacher;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class StudentRepositoryPostgres implements Repository<Student> {

    private final Datasource datasource = Datasource.getInstance();
    private static final String SQL_GET_ALL_STUDENT_COLUMNS =
            "select\n" +
                    "s.id as student_id, s.first_name as student_first_name, s.last_name as student_last_name , " +
                    "s.age as student_age , s.login as student_login, s.\"password\" as student_password,\n" +
                    "t.id as teacher_id, t.first_name as teacher_first_name, t.last_name as teacher_last_name , " +
                    "t.age as teacher_age, t.login as teacher_login , t.\"password\" as teacher_password,\n" +
                    "g.id group_id, g.\"name\" as group_name\n" +
                    "from model3.student s\n" +
                    "left join model3.student_group sg\n" +
                    "on s.id = sg.student_id\n" +
                    "left join model3.\"group\" g\n" +
                    "on sg.group_id = g.id\n" +
                    "left join model3.teacher t\n" +
                    "on t.id = g.teacher_id\n";
    private static final String SQL_GET_STUDENT_BY_ID = SQL_GET_ALL_STUDENT_COLUMNS + " where s.id = ?";

    private static class StudentRepositoryPostgresHolder {
        private final static StudentRepositoryPostgres HOLDER_INSTANCE = new StudentRepositoryPostgres();
    }

    public static StudentRepositoryPostgres getInstance() {
        return StudentRepositoryPostgresHolder.HOLDER_INSTANCE;
    }

    @Override
    public Set<Student> findAll() {
        Map<Integer, Student> mapStudent = new HashMap<>();
        Map<Integer, Group> mapGroup = new HashMap<>();
        Map<Integer, Teacher> mapTeacher = new HashMap<>();
        try (Connection conn = datasource.getConnection();
             PreparedStatement stat = conn.prepareStatement(SQL_GET_ALL_STUDENT_COLUMNS);
             ResultSet res = stat.executeQuery()) {
            while (res.next()) {
                Integer teacherId = res.getInt(SqlColumns.TEACHER_ID);

                Integer studentId = res.getInt(SqlColumns.STUDENT_ID);
                Integer groupId = res.getInt(SqlColumns.GROUP_ID);

                mapTeacher.putIfAbsent(teacherId, new Teacher()
                        .withAge(res.getInt(SqlColumns.TEACHER_AGE))
                        .withLogin(res.getString(SqlColumns.TEACHER_LOGIN))
                        .withPassword(res.getString(SqlColumns.TEACHER_PASSWORD))
                        .withFirst_name(res.getString(SqlColumns.TEACHER_FIRST_NAME))
                        .withLast_name(res.getString(SqlColumns.TEACHER_LAST_NAME))
                        .withId(res.getInt(SqlColumns.TEACHER_ID)));

                mapStudent.putIfAbsent(studentId, new Student()
                        .withAge(res.getInt(SqlColumns.STUDENT_AGE))
                        .withLogin(res.getString(SqlColumns.STUDENT_LOGIN))
                        .withPassword(res.getString(SqlColumns.STUDENT_PASSWORD))
                        .withFirst_name(res.getString(SqlColumns.STUDENT_FIRST_NAME))
                        .withLast_name(res.getString(SqlColumns.STUDENT_LAST_NAME))
                        .withId(res.getInt(SqlColumns.STUDENT_ID)));

                mapGroup.putIfAbsent(groupId, new Group()
                        .withName(res.getString(SqlColumns.GROUP_NAME))
                        .withId(res.getInt(SqlColumns.GROUP_ID))
                        .withTeacher(mapTeacher.getOrDefault(teacherId,
                                new Teacher()
                                    .withAge(res.getInt(SqlColumns.TEACHER_AGE))
                                    .withLogin(res.getString(SqlColumns.TEACHER_LOGIN))
                                    .withPassword(res.getString(SqlColumns.TEACHER_PASSWORD))
                                    .withFirst_name(res.getString(SqlColumns.TEACHER_FIRST_NAME))
                                    .withLast_name(res.getString(SqlColumns.TEACHER_LAST_NAME))
                                    .withId(res.getInt(SqlColumns.TEACHER_ID)))));

                mapTeacher.computeIfPresent(teacherId, (id, teacher) ->
                        teacher
                                .withGroup(mapGroup.get(groupId)));

                mapStudent.computeIfPresent(studentId, (id, student) ->
                        student
                                .withGroup(mapGroup.get(groupId)));

                mapGroup.computeIfPresent(groupId, (id, group) ->
                        group
                                .withStudent(mapStudent.get(studentId))
                                .withTeacher(mapTeacher.get(teacherId)));

            }

        } catch (SQLException e) {
            log.error("sql error, during reding students");
            throw new DatabaseException(e);
        } catch (ClassNotFoundException e) {
            log.error("student ");
            e.printStackTrace();
        }
        Set<Student> result = new HashSet<>(mapStudent.values());
        return result;
    }

    @Override
    public Optional<Student> find(Integer id) {
        Student student = null;
        try (Connection conn = datasource.getConnection();
             PreparedStatement pStat = conn.prepareStatement(SQL_GET_STUDENT_BY_ID);
        ) {
            pStat.setInt(1, id);
            ResultSet rs = pStat.executeQuery();
            if (rs.next()) {
                rs.getRow();
                student = new Student()
                        .withAge(rs.getObject(SqlColumns.STUDENT_AGE, Integer.class))
                        .withLogin(rs.getString(SqlColumns.STUDENT_LOGIN))
                        .withPassword(rs.getString(SqlColumns.STUDENT_PASSWORD))
                        .withFirst_name(rs.getString(SqlColumns.STUDENT_FIRST_NAME))
                        .withLast_name(rs.getString(SqlColumns.STUDENT_LAST_NAME))
                        .withId(rs.getInt(SqlColumns.STUDENT_ID));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(student);
    }

    private static <V, K> V putIfAbsentAndReturn (K key, V value, Map<K, V> map) {
        if (key == null){
            return null;
        }
        map.putIfAbsent(key, value);
        return map.get(key);
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
