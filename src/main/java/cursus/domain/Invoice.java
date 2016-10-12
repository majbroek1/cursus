package cursus.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Created by maart on 10-10-2016.
 */
@Data
@Builder
public class Invoice {
    private String name;
    private String courseName;
    private String courseCode;
    private String companyName;
    private String email;
    private String accountNumber;

}
