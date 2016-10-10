package cursus.domain;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maart on 10-10-2016.
 */
//@RunWith(MockitoJUnitRunner.class)
public class CourseTest {

    @Test
    public void calculateEndDate(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateStart = LocalDate.parse("2016/10/15",df);
        LocalDate dateEnd = LocalDate.parse("2016/10/17",df);
        int days = 2;

        Course course = Course.builder().date(dateStart).duration(days).build();

        assertThat(course.getEndDate(),is(dateEnd));
    }

}