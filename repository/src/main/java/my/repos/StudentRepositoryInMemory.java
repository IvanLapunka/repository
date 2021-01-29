package my.repos;

import my.pojo.Student;

public class StudentRepositoryInMemory extends AbstractRepositoryInMemory<Student> implements StudentRepository {
    private static int id;
    private static volatile StudentRepositoryInMemory instance;
    private StudentRepositoryInMemory() {
        map.put(1, new Student()
                .withId(1)
                .withAge(25)
                .withLogin("student_1")
                .withPassword("pass_1")
                .withFirst_name("first_name_1")
                .withLast_name("last_name_1"));
        map.put(2, new Student()
                .withId(2)
                .withAge(27)
                .withLogin("student_2")
                .withPassword("pass_2")
                .withFirst_name("first_name_2")
                .withLast_name("last_name_2"));
        map.put(3, new Student()
                .withId(3)
                .withAge(32)
                .withLogin("student_3")
                .withPassword("pass_3")
                .withFirst_name("first_name_3")
                .withLast_name("last_name_3"));
    }

    @Override
    protected synchronized Integer generateId() {
        return id++;
    }

    public static StudentRepositoryInMemory getInstance() {
        if (instance == null) {
            synchronized (StudentRepositoryInMemory.class) {
                if (instance == null) {
                    instance = new StudentRepositoryInMemory();
                }
            }
        }
        return instance;
    }
}
