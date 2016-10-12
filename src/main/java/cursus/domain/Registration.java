package cursus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by maart on 10-10-2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    private Course course;
    private Student student;
    private boolean business;

}
