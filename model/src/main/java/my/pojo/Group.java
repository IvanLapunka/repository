package my.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public abstract class Group extends AbstractEntity {
    private String name;
    private Teacher teacher;
    private Set<Student> students = new HashSet<>();
    private Set<Theme> themes = new HashSet<>();

    public Group(int id, String name, Teacher teacher) {
        super(id);
        this.name = name;
        this.teacher = teacher;
    }

    @Override
    public Group withId(int id) {
        this.setId(id);
        return this;
    }
}
