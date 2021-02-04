package my.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary extends AbstractEntity {
    private int salary;

    @Override
    public Salary withId(Integer id) {
        this.setId(id);
        return this;
    }
}
