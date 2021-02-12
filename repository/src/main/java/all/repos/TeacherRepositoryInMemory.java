package all.repos;

import exceptions.pojo.Teacher;

public class TeacherRepositoryInMemory extends AbstractRepositoryInMemory<Teacher> implements TeacherRepository {
    private static int id = 1;

    private TeacherRepositoryInMemory() {
        Teacher teacher1 = new Teacher()
                .withLogin("java")
                .withPassword("pass_java")
                .withFirst_name("Fiodor")
                .withLast_name("Konuhov")
                .withAge(35);
        Teacher teacher2 = new Teacher()
                .withLogin("python")
                .withPassword("pass_payton")
                .withFirst_name("Piotor")
                .withLast_name("Korotkov")
                .withAge(40);
        Teacher teacher3 = new Teacher()
                .withLogin("c++")
                .withPassword("pass++")
                .withFirst_name("Sergei")
                .withLast_name("Sergeevich")
                .withAge(45);
        save(teacher1);
        save(teacher2);
        save(teacher3);

    }

    private static class TeacherRepositoryHolder {
        private static final TeacherRepositoryInMemory INSTANCE_HOLDER = new TeacherRepositoryInMemory();
    }

    public static TeacherRepositoryInMemory getInstance() {
        return TeacherRepositoryHolder.INSTANCE_HOLDER;
    }

    @Override
    protected Integer generateId() {
        return id++;
    }
}
