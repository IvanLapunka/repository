package exceptions.pojo;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends AbstractEntity {
    private String login;
    private String password;
    private String first_name;
    private String last_name;
    private int age;

    public Admin (int id, String login, String password, String first_name, String last_name, int age) {
        super(id);
        this.login = login;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
    }

    @Override
    public Admin withId(Integer id) {
        setId(id);
        return this;
    }
}
