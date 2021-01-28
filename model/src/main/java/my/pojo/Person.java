package my.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Person extends AbstractEntity {
    private String login;
    private String password;
    private String first_name;
    private String last_name;
    private int age;

    public Person(int id, String login, String password, String first_name, String last_name, int age) {
        super(id);
        this.login = login;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
