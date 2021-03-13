package all.repos;


import by.pojo.Group;
import by.pojo.Student;
import by.pojo.Teacher;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class StudentRepositoryPostgres extends AbstractRepositoryPostgres<Student> implements StudentRepository {

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

    @Override
    protected Set<Student> getItems(ResultSet res) throws SQLException {
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

    @Override
    protected String getSqlAllItems() {
        return SQL_GET_ALL_STUDENT_COLUMNS;
    }

    @Override
    protected String getSqlItemById() {
        return SQL_GET_STUDENT_BY_ID;
    }

    @Override
    protected String getSqlUpdate() {
          return  "update model3.student " +
            "set login = ?, " +
            "password = ?, " +
            "first_name = ?, " +
            "last_name = ?, " +
            "age = ? " +
            "where id = ? ";
    }

    @Override
    protected Student insert(Student entity) {
        final Student student = entity.getId() == null ? super.insert(entity) : super.update(entity);
        try(final Connection connection = datasource.getConnection()) {
            connection.setAutoCommit(false);
            try(final PreparedStatement preparedStatement = connection.prepareStatement("insert into model3.student_group(group_id, student_id)" +
                    "values (?, ?)");
                PreparedStatement preparedStatement1 = connection.prepareStatement("select 1 from model3.student_group where group_id = ? and student_id = ?");
            ) {
                final Set<Group> groups = entity.getGroups();
                Integer studentId = entity.getId();
                for(Group group: groups.stream().collect(Collectors.toList())) {
                    prepareStudentGroup(preparedStatement1, group.getId(), studentId);
                    final ResultSet resultSet = preparedStatement1.executeQuery();
                    if (!resultSet.next()) {
                        prepareStudentGroup(preparedStatement, group.getId(), studentId);
                        preparedStatement.executeUpdate();
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return super.insert(entity);
    }

    private void prepareStudentGroup(PreparedStatement preparedStatement1, Integer groupId, Integer studentId) throws SQLException {
        preparedStatement1.setInt(1, groupId);
        preparedStatement1.setInt(2, studentId);
    }

    @Override
    protected void setPrepareInsertStatementParameters(PreparedStatement ps, Student entity) throws SQLException {
        ps.setString(1, entity.getLogin());
        ps.setString(2, entity.getPassword());
        ps.setString(3, entity.getFirst_name());
        ps.setString(4, entity.getLast_name());
        ps.setInt(5, entity.getAge());
    }

    @Override
    protected void setPrepareUpdateStatementParameters(PreparedStatement ps, Student entity) throws SQLException {
        ps.setString(1, entity.getLogin());
        ps.setString(2, entity.getPassword());
        ps.setString(3, entity.getFirst_name());
        ps.setString(4, entity.getLast_name());
        ps.setInt(5, entity.getAge());
        ps.setInt(6, entity.getId());
    }

    @Override
    protected String getSqlInsert() {
        return "insert into model3.student(login, password, first_name, last_name, age)" +
                "values(?, ?, ?, ?, ?) RETURNING id";
    }

    @Override
    protected String getSqlDeleteById() {
        return "delete from model3.student where id = ?";
    }

    private static class StudentRepositoryPostgresHolder {
        private final static StudentRepositoryPostgres HOLDER_INSTANCE = new StudentRepositoryPostgres();
    }

    public static StudentRepositoryPostgres getInstance() {
        return StudentRepositoryPostgresHolder.HOLDER_INSTANCE;
    }
}
