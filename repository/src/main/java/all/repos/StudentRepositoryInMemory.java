package all.repos;

import by.pojo.Student;

public class StudentRepositoryInMemory extends AbstractRepositoryInMemory<Student> implements StudentRepository {
    private static int id = 1;
    private static volatile StudentRepositoryInMemory instance;
    private StudentRepositoryInMemory() {
        save(new Student()
                .withId(1)
                .withLogin("student_1")
                .withPassword("pass_1")
                .withFirst_name("first_name_1")
                .withLast_name("last_name_1")
                .withAge(25));
        save(new Student()
                .withId(2)
                .withAge(27)
                .withLogin("student_2")
                .withPassword("pass_2")
                .withFirst_name("first_name_2")
                .withLast_name("last_name_2"));
        save(new Student()
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
