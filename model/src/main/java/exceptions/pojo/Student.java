package exceptions.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
public class Student extends AbstractEntity {
    private String login;
    private String password;
    private String first_name;
    private String last_name;
    @JsonManagedReference
    private Set<Group> groups = new HashSet<>();
    private Integer age;

    public Student(int id, String login, String password, String first_name, String last_name, int age, Set<Group> groups) {
        super(id);
        this.login = login;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.groups = groups;
    }

    @Override
    public Student withId(Integer id) {
        setId(id);
        return this;
    }

    public Student withGroup(Group group) {
        groups.add(group);
        return this;
    }
}
