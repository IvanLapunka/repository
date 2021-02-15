package all.repos;

import exceptions.pojo.Group;
import exceptions.pojo.Student;
import exceptions.pojo.Teacher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TeacherRepositoryPostgres extends AbstractRepositoryPostgres<Teacher> implements TeacherRepository {

    private static final String SQL_GET_ALL_TEACHERS =
            "select\n" +
            "s.id as student_id, s.first_name as student_first_name, s.last_name as student_last_name , " +
            "s.age as student_age , s.login as student_login, s.\"password\" as student_password,\n" +
            "t.id as teacher_id, t.first_name as teacher_first_name, t.last_name as teacher_last_name , " +
            "t.age as teacher_age, t.login as teacher_login , t.\"password\" as teacher_password,\n" +
            "g.id group_id, g.\"name\" as group_name\n" +
            "FROM model3.teacher t \t" +
            "left join model3.\"group\" g \t" +
            "on t.id = g.teacher_id \t" +
            "left join model3.student_group sg \t" +
            "on g.id = sg.group_id \t" +
            "left join model3.student s \t" +
            "on sg.student_id = s.id \t";

    private static final String SQL_GET_TEACHER_BY_ID = SQL_GET_ALL_TEACHERS + " where t.id = ?";

    @Override
    protected Set<Teacher> getItems(ResultSet res) throws SQLException {
        Map<Integer, Teacher> teacherMap = new HashMap();
        Map<Integer, Student> studentMap = new HashMap<>();
        Map<Integer, Group> groupMap = new HashMap<>();
        while (res.next()) {
            final Integer teacherId = getRsInteger(res, SqlColumns.TEACHER_ID);
            final Integer studentId = getRsInteger(res, SqlColumns.STUDENT_ID);
            final Integer groupId = getRsInteger(res, SqlColumns.GROUP_ID);
            teacherMap.putIfAbsent(teacherId, new Teacher()
                    .withAge(getRsInteger(res, SqlColumns.TEACHER_AGE))
                    .withFirst_name(getRsString(res, SqlColumns.TEACHER_FIRST_NAME))
                    .withLast_name(getRsString(res, SqlColumns.TEACHER_LAST_NAME))
                    .withLogin(getRsString(res, SqlColumns.TEACHER_LOGIN))
                    .withPassword(getRsString(res, SqlColumns.TEACHER_PASSWORD))
                    .withId(getRsInteger(res, SqlColumns.TEACHER_ID))
                    .withGroup(putIfAbsentAndReturn(groupMap, groupId, new Group()
                            .withName(getRsString(res, SqlColumns.GROUP_NAME))
                            .withId(getRsInteger(res, SqlColumns.GROUP_ID))
                            .withStudent(putIfAbsentAndReturn(studentMap, studentId, new Student()
                                        .withAge(getRsInteger(res, SqlColumns.STUDENT_AGE))
                                        .withFirst_name(getRsString(res, SqlColumns.STUDENT_FIRST_NAME))
                                        .withLast_name(getRsString(res, SqlColumns.STUDENT_LAST_NAME))
                                        .withLogin(getRsString(res, SqlColumns.STUDENT_LOGIN))
                                        .withPassword(getRsString(res, SqlColumns.STUDENT_PASSWORD))
                                        .withId(getRsInteger(res, SqlColumns.STUDENT_ID))
                                    ))
                            ))
                    );

            groupMap.computeIfPresent(groupId, (id, group) ->
                    group.withTeacher(teacherMap.get(teacherId)));

            studentMap.computeIfPresent(studentId, (id, student) ->
                    student.withGroup(groupMap.get(groupId)));
        }
        return new HashSet<>(teacherMap.values());
    }

    @Override
    protected String getSqlAllItems() {
        return SQL_GET_ALL_TEACHERS;
    }

    @Override
    protected String getSqlItemById() {
        return SQL_GET_TEACHER_BY_ID;
    }

    @Override
    protected String getSqlUpdate() {
        return "update model3.teacher" +
                "set login = ?," +
                "password = ?," +
                "first_name = ?," +
                "last_name = ?," +
                "age = ?" +
                "where id = ?";
    }

    @Override
    protected void setPrepareUpdateStatementParameters(PreparedStatement ps, Teacher entity) throws SQLException {
        ps.setString(1, entity.getLogin());
        ps.setString(2, entity.getPassword());
        ps.setString(3, entity.getFirst_name());
        ps.setString(4, entity.getLast_name());
        ps.setInt(5, entity.getAge());
        ps.setInt(6, entity.getId());
    }

    @Override
    protected String getSqlInsert() {
        return "insert into model3.teacher(login, password, first_name, last_name, age)" +
                "values(?, ?, ?, ?, ?) RETURNING id";
    }

    @Override
    protected void setPrepareInsertStatementParameters(PreparedStatement ps, Teacher entity) throws SQLException {
        ps.setString(1, entity.getLogin());
        ps.setString(2, entity.getPassword());
        ps.setString(3, entity.getFirst_name());
        ps.setString(4, entity.getLast_name());
        ps.setInt(5, entity.getAge());
    }

    @Override
    protected String getSqlDeleteById() {
        return "delete from model3.teacher where id = ?";
    }

    private TeacherRepositoryPostgres() {

    }

    private static class TeacherRepositoryPostgresHolder {
        private final static TeacherRepositoryPostgres INSTANCE_HOLDER = new TeacherRepositoryPostgres();
    }

    public static TeacherRepositoryPostgres getInstance() {
        return TeacherRepositoryPostgresHolder.INSTANCE_HOLDER;
    }
}
