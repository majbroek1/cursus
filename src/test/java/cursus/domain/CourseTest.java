package cursus.domain;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * Created by maart on 11-10-2016.
 */
public class CourseTest {

    @Test
    public void calculateEndDate2Days(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateStart = LocalDate.parse("2016/10/15",df);
        LocalDate dateEnd = LocalDate.parse("2016/10/17",df);
        int days = 2;

        Course course = Course.builder().date(dateStart).duration(days).build();

        assertThat(course.getEndDate(),is(dateEnd));
    }

    @Test
    public void calculateEndDate0Days(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateStart = LocalDate.parse("2016/10/15",df);
        int days = 0;

        Course course = Course.builder().date(dateStart).duration(days).build();

        assertThat(course.getEndDate(),is(dateStart));
    }
}