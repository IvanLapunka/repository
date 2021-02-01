package my.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class Teacher extends AbstractEntity {
    private List<Salary> salaries = new ArrayList<>();
    Group group;
    private String login;
    private String password;
    private String first_name;
    private String last_name;
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
    public Teacher withId(int id) {
        setId(id);
        return this;
    }
}
