package cursus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by maart on 10-10-2016.
 */
@Data
@AllArgsConstructor
public class Registration {

    private Course course;
    private Student student;
    private boolean business;

}
