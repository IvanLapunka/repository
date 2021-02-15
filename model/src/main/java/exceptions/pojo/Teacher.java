package exceptions.pojo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Teacher extends AbstractEntity {
    private List<Salary> salaries = new ArrayList<>();
    private String login;
    private String password;
    private String first_name;
    private String last_name;
    private Group group;
    private int age;

    public Teacher(int id, String login, String password, String first_name, String last_name, int age) {
        super(id);
        this.login = login;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
    }

    @Override
    public Teacher withId(Integer id) {
        setId(id);
        return this;
    }

}
