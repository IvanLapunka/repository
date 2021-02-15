package all.repos;

import exceptions.pojo.Group;
import exceptions.pojo.Student;
import exceptions.pojo.Teacher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GroupRepositoryPostgres extends AbstractRepositoryPostgres<Group> implements GroupRepository{
    private static final String SQL_GET_ALL_GROUPS =
            "select\n" +
                    "s.id as student_id, s.first_name as student_first_name, s.last_name as student_last_name , " +
                    "s.age as student_age , s.login as student_login, s.\"password\" as student_password,\n" +
                    "t.id as teacher_id, t.first_name as teacher_first_name, t.last_name as teacher_last_name , " +
                    "t.age as teacher_age, t.login as teacher_login , t.\"password\" as teacher_password,\n" +
                    "g.id group_id, g.\"name\" as group_name\n" +
                    "FROM model3.\"group\" g \t" +
                    "left join model3.teacher t \t" +
                    "on t.id = g.teacher_id \t" +
                    "left join model3.student_group sg \t" +
                    "on g.id = sg.group_id \t" +
                    "left join model3.student s \t" +
                    "on sg.student_id = s.id \t";

    private static final String SQL_GET_GROUP_BY_ID = SQL_GET_ALL_GROUPS + " where t.id = ?";

    private GroupRepositoryPostgres() {

    }

    private static final class GroupRepositoryPostgresHolder {
        private static final GroupRepositoryPostgres INSTANCE_HOLDER = new GroupRepositoryPostgres();
    }

    public static GroupRepositoryPostgres getInstance() {
        return GroupRepositoryPostgresHolder.INSTANCE_HOLDER;
    }

    @Override
    protected Set<Group> getItems(ResultSet res) throws SQLException {
        Map<Integer, Teacher> teacherMap = new HashMap();
        Map<Integer, Student> studentMap = new HashMap<>();
        Map<Integer, Group> groupMap = new HashMap<>();
        while (res.next()) {
            Integer teacherId = getRsInteger(res, SqlColumns.TEACHER_ID);
            Integer studentId = getRsInteger(res, SqlColumns.STUDENT_ID);
            Integer groupId = getRsInteger(res, SqlColumns.GROUP_ID);

            groupMap.putIfAbsent(groupId, new Group()
                    .withName(getRsString(res, SqlColumns.GROUP_NAME))
                    .withTeacher(putIfAbsentAndReturn(teacherMap, teacherId, new Teacher()
                            .withAge(getRsInteger(res, SqlColumns.TEACHER_AGE))
                            .withFirst_name(getRsString(res, SqlColumns.TEACHER_FIRST_NAME))
                            .withLast_name(getRsString(res, SqlColumns.TEACHER_LAST_NAME))
                            .withLogin(getRsString(res, SqlColumns.TEACHER_LOGIN))
                            .withPassword(getRsString(res, SqlColumns.TEACHER_PASSWORD))
                            .withId(getRsInteger(res, SqlColumns.TEACHER_ID))))
                    .withStudent(putIfAbsentAndReturn(studentMap, studentId, new Student()
                            .withAge(res.getInt(SqlColumns.STUDENT_AGE))
                            .withLogin(res.getString(SqlColumns.STUDENT_LOGIN))
                            .withPassword(res.getString(SqlColumns.STUDENT_PASSWORD))
                            .withFirst_name(res.getString(SqlColumns.STUDENT_FIRST_NAME))
                            .withLast_name(res.getString(SqlColumns.STUDENT_LAST_NAME))
                            .withId(res.getInt(SqlColumns.STUDENT_ID)))));

            teacherMap.computeIfPresent(teacherId, (id, teacher) ->
                    teacher
                            .withGroup(groupMap.get(groupId)));
            studentMap.computeIfPresent(studentId, (id, student) ->
                    student
                            .withGroup(groupMap.get(groupId)));
        }
        return new HashSet<Group>(groupMap.values());
    }

    @Override
    protected String getSqlAllItems() {
        return SQL_GET_ALL_GROUPS;
    }

    @Override
    protected String getSqlItemById() {
        return SQL_GET_GROUP_BY_ID;
    }

    @Override
    protected String getSqlUpdate() {
        return  "update model3.group" +
                "set name = ?," +
                "teacher_id = ?" +
                "where id = ?";
    }

    @Override
    protected void setPrepareUpdateStatementParameters(PreparedStatement ps, Group entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getTeacher().getId());
        ps.setInt(3, entity.getId());
    }

    @Override
    protected String getSqlInsert() {
        return "insert into model3.group(name, teacher_id)" +
                "values(?, ?) RETURNING id";
    }

    @Override
    protected void setPrepareInsertStatementParameters(PreparedStatement ps, Group entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getTeacher().getId());
    }

    @Override
    protected String getSqlDeleteById() {
        return "delete from model3.group where id = ?";
    }
}
