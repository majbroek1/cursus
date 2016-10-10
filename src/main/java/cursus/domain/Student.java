package cursus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by maart on 10-10-2016.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private int id;
    private String name;
    private String lastName;
    private String email;
    private String accountNumber;
    private String address;
    private Company company;

}
