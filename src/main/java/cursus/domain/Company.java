package cursus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by maart on 10-10-2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    private int id;
    private String name;
    private String accountNumber;
    private String email;
    private String address;
}
