package my.pojo;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@With
public class Student extends AbstractEntity {
    private String login;
    private String password;
    private String first_name;
    private String last_name;
    private int age;

    public Student(int id, String login, String password, String first_name, String last_name, int age) {
        super(id);
        this.login = login;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
    }

    @Override
    public Student withId(int id) {
        this.setId(id);
        return this;
    }
}
