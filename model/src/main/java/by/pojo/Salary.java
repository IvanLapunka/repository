package by.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salary", schema = "model3")
public class Salary extends AbstractEntity {
    @Column(name = "amount")
    private Integer salary;
    private Integer dividor;

    @Override
    public Salary withId(Integer id) {
        this.setId(id);
        return this;
    }
}
