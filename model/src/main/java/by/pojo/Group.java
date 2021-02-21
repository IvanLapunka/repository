package by.pojo;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"students", "teacher"})
@ToString(callSuper = true, exclude = {"students", "teacher"} )
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Group extends AbstractEntity {
    private String name;
    private Teacher teacher;
    private Set<Student> students = new HashSet<>();
    //private Set<Theme> themes = new HashSet<>();

    public Group(int id, String name, Teacher teacher) {
        super(id);
        this.name = name;
        this.teacher = teacher;
    }

    @Override
    public Group withId(Integer id) {
        setId(id);
        return this;
    }

    public Group withStudent(Student student) {
        students.add(student);
        return this;
    }

    public Group withName(String name) {
        setName(name);
        return this;
    }

    public Group withTeacher(Teacher teacher) {
        setTeacher(teacher);
        return this;
    }
}
