package my.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends Person {
    private List<Salary> salaries = new ArrayList<>();
    Group group;
}
