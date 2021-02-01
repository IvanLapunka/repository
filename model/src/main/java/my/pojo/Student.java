package my.pojo;

import lombok.*;

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
        this.id = id;
        return this;
    }

//    public Student withAge(int age) {
//        setAge(age);
//        return this;
//    }
//
//    public Student withLogin(String login) {
//        setLogin(login);
//        return this;
//    }
//
//    public Student withPassword(String password) {
//        setPassword(password);
//        return this;
//    }
//
//    public Student withFirst_name(String first_name) {
//        setFirst_name(first_name);
//        return this;
//    }
//
//    public Student withLast_name(String last_name) {
//        setLast_name(last_name);
//        return this;
//    }
}
