package cursus.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created by maart on 10-10-2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    private int id;
    private String name;
    private LocalDate date;
    private int duration;
    private String courseCode;

    public LocalDate getEndDate() {
        return date.plusDays(duration);
    }

}
