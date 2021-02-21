package by.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary extends AbstractEntity {
    private Integer salary;
    private Integer dividor;

    @Override
    public Salary withId(Integer id) {
        this.setId(id);
        return this;
    }
}
