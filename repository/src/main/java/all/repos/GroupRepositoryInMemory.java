package all.repos;

import exceptions.pojo.Group;
import exceptions.pojo.Student;
import exceptions.pojo.Teacher;

import java.util.Optional;

public class GroupRepositoryInMemory extends AbstractRepositoryInMemory<Group> implements GroupRepository{
    TeacherRepository teacherRepository = TeacherRepositoryInMemory.getInstance();
    StudentRepository studentRepository = StudentRepositoryInMemory.getInstance();

    private static int id = 1;

    private GroupRepositoryInMemory() {

    }

    private static class GroupRepositoryInMemoryHolder {
        public static final GroupRepositoryInMemory INSTANCE_HOLDER = new GroupRepositoryInMemory();
    }

    public static GroupRepositoryInMemory getInstance() {
        return GroupRepositoryInMemoryHolder.INSTANCE_HOLDER;
    }

    @Override
    protected Integer generateId() {
        return id++;
    }

    {
        Optional<Student> student1 = studentRepository.find(1);
        Optional<Student> student2 = studentRepository.find(2);
        Optional<Student> student3 = studentRepository.find(3);

        Optional<Teacher> teacher = teacherRepository.find(1);
        Optional<Teacher> teacher1 = teacherRepository.find(2);

        Group group1 = new Group()
                .withName("java group name")
                .withTeacher(teacher.get())
                .withStudent(student1.get())
                .withStudent(student2.get());


        Group group2 = new Group()
                .withName("python group name")
                .withTeacher(teacher1.get())
                .withStudent(student3.get())
                .withStudent(student2.get());

        save(group1);
        save(group2);

        teacher.get().setGroup(group1);
        teacher1.get().setGroup(group2);

    }
}
