package by.pojo;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.mapping.ToOne;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, exclude = {"students", "teacher"})
@ToString(callSuper = true, exclude = { "teacher"} )
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@Entity
@Table(name = "group", schema = "model3")
public class Group extends AbstractEntity {
    private String name;
    @OneToOne(
            fetch = FetchType.LAZY,
            optional = true,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(unique = true, name = "teacher_id")
    private Teacher teacher;

    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable (
            schema = "model3",
            name = "student_group",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")}
    )
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
