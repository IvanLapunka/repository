package my.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theme extends AbstractEntity {
    private Set<Group> groups;
    private String name;

    @Override
    public AbstractEntity withId(Integer id) {
        this.setId(id);
        return this;
    }
}
