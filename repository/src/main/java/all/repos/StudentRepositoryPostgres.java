package all.repos;

import exceptions.DatabaseException;
import exceptions.pojo.Group;
import exceptions.pojo.Student;
import exceptions.pojo.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Integers;

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
        Set<Student> result = new HashSet<>();
        try (Connection conn = datasource.getConnection();
             PreparedStatement stat = conn.prepareStatement(SQL_GET_ALL_STUDENT_COLUMNS);
             ResultSet res = stat.executeQuery()) {
             result = rsToStudents(res);
        } catch (SQLException e) {
            log.error("sql error, during reading students");
            throw new DatabaseException(e);
        } catch (ClassNotFoundException e) {
            log.error("student ");
            e.printStackTrace();
        }
        return result;
    }

    private Set<Student> rsToStudents(ResultSet res) throws SQLException {
        Map<Integer, Student> mapStudent = new HashMap<>();
        Map<Integer, Group> mapGroup = new HashMap<>();
        Map<Integer, Teacher> mapTeacher = new HashMap<>();
        while (res.next()) {
            Integer teacherId = getRsInteger(res, SqlColumns.TEACHER_ID);
            Integer studentId = getRsInteger(res, SqlColumns.STUDENT_ID);
            Integer groupId = getRsInteger(res, SqlColumns.GROUP_ID);


            mapStudent.putIfAbsent(studentId, new Student()
                    .withAge(res.getInt(SqlColumns.STUDENT_AGE))
                    .withLogin(res.getString(SqlColumns.STUDENT_LOGIN))
                    .withPassword(res.getString(SqlColumns.STUDENT_PASSWORD))
                    .withFirst_name(res.getString(SqlColumns.STUDENT_FIRST_NAME))
                    .withLast_name(res.getString(SqlColumns.STUDENT_LAST_NAME))
                    .withId(res.getInt(SqlColumns.STUDENT_ID))
                    .withGroup(putIfAbsentAndReturn(mapGroup, groupId, new Group()
                                .withName(res.getString(SqlColumns.GROUP_NAME))
                                .withId(getRsInteger(res, SqlColumns.GROUP_ID))
                                .withTeacher(putIfAbsentAndReturn(mapTeacher, teacherId, new Teacher()
                                            .withAge(res.getInt(SqlColumns.TEACHER_AGE))
                                            .withLogin(res.getString(SqlColumns.TEACHER_LOGIN))
                                            .withPassword(res.getString(SqlColumns.TEACHER_PASSWORD))
                                            .withFirst_name(res.getString(SqlColumns.TEACHER_FIRST_NAME))
                                            .withLast_name(res.getString(SqlColumns.TEACHER_LAST_NAME))
                                            .withId(getRsInteger(res, SqlColumns.TEACHER_ID)))))));

            mapStudent.computeIfPresent(studentId, (id, student) ->
                    student
                            .withGroup(mapGroup.get(groupId)));

            mapTeacher.computeIfPresent(teacherId, (id, teacher) ->
                    teacher
                            .withGroup(mapGroup.get(groupId)));

            mapGroup.computeIfPresent(groupId, (id, group) ->
                    group
                            .withStudent(mapStudent.get(studentId)));

        }
        return new HashSet<>(mapStudent.values());
    }

    private static Integer getRsInteger(ResultSet rs, String colName) throws SQLException {
        return rs.getObject(colName, Integer.class);
    }

    @Override
    public Optional<Student> find(Integer id) {
        Set<Student> result = new HashSet<>();
        try (Connection conn = datasource.getConnection();
             PreparedStatement stat = conn.prepareStatement(SQL_GET_STUDENT_BY_ID);
             ResultSet res = stat.executeQuery()) {
             result = rsToStudents(res);
        } catch (SQLException e) {
            log.error("sql error, during reading students");
            throw new DatabaseException(e);
        } catch (ClassNotFoundException e) {
            log.error("driver class not found");
            e.printStackTrace();
        }
        return result.stream().findAny();
    }

    private static <V, K> V putIfAbsentAndReturn (Map<K, V> map, K key, V value) {
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
