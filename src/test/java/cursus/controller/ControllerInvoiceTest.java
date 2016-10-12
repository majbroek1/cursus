package cursus.controller;

import cursus.dal.courseimport.CourseImporter;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.Course;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import static cursus.TestBuilders.testCourseBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by maart on 12-10-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerInvoiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RepositoryOracle repo;

    @Mock
    private CourseImporter impo;

    @InjectMocks
    private Controller controller;



    @Test
    public void getCoursesFromWeekNull() throws SQLException {
        thrown.expect(NumberFormatException.class);
        controller.getCoursesFromWeek(null);
    }

    @Test
    public void getCoursesFromWeekWrongValue() throws SQLException {
        thrown.expect(NumberFormatException.class);
        controller.getCoursesFromWeek("abc");
    }

    @Test
    public void getCoursesFromWeek() throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateGood = LocalDate.parse("2016/10/15", df);
        LocalDate dateBad = LocalDate.parse("2016/11/15", df);

        Course goodCourse = Course.builder().date(dateGood).build();
        Course badCourse = Course.builder().date(dateBad).build();
        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(goodCourse, goodCourse, badCourse)));

        assertThat(controller.getCoursesFromWeek("41").size(), is(2));
    }

    @Test
    public void getNoCoursesFromWeek() throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateBad = LocalDate.parse("2016/11/15", df);

        Course badCourse = Course.builder().date(dateBad).build();
        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(badCourse, badCourse, badCourse)));

        assertThat(controller.getCoursesFromWeek("41").size(), is(0));
    }

    @Test
    public void addCoursesNullOrInvalidPath() throws Exception {
        thrown.expect(NullPointerException.class);
        when(impo.getCoursesFromFile("")).thenReturn(null);

        assertThat(controller.addCourses(""), is(false));
    }

    @Test
    public void addCourses() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/11/15", df);
        LocalDate secondDate = LocalDate.parse("2016/11/18", df);

        Course course1 = testCourseBuilder().date(firstDate).build();
        Course course2 = testCourseBuilder().date(secondDate).build();

        when(repo.getAllCourses()).thenReturn(new ArrayList<>());
        when(impo.getCoursesFromFile("")).thenReturn(new ArrayList<>(Arrays.asList(course1, course2)));

        assertThat(controller.addCourses(""), is(true));
    }

    @Test
    public void addCourseBeginDateTwoSameTypesInUsedTimeSlot() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/11/15", df);
        LocalDate secondDate = LocalDate.parse("2016/11/18", df);

        Course course1 = testCourseBuilder().date(firstDate).duration(5).courseCode("AAA").build();
        Course course2 = testCourseBuilder().date(secondDate).duration(5).courseCode("AAA").build();

        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(course1)));
        when(impo.getCoursesFromFile("")).thenReturn(new ArrayList<>(Arrays.asList(course2)));

        assertThat(controller.addCourses(""), is(false));
    }

    @Test
    public void addCourseEndDateTwoSameTypesInUsedTimeSlot() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/11/15", df);
        LocalDate secondDate = LocalDate.parse("2016/11/12", df);

        Course course1 = testCourseBuilder().date(firstDate).duration(5).courseCode("AAA").build();
        Course course2 = testCourseBuilder().date(secondDate).duration(5).courseCode("AAA").build();

        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(course1)));
        when(impo.getCoursesFromFile("")).thenReturn(new ArrayList<>(Arrays.asList(course2)));

        assertThat(controller.addCourses(""), is(false));
    }

    @Test
    public void addCourseStartDateSameTwoSameTypes() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/11/15", df);

        Course course1 = testCourseBuilder().date(firstDate).duration(5).courseCode("AAA").build();
        Course course2 = testCourseBuilder().date(firstDate).duration(5).courseCode("AAA").build();

        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(course1)));
        when(impo.getCoursesFromFile("")).thenReturn(new ArrayList<>(Arrays.asList(course2)));

        assertThat(controller.addCourses(""), is(false));
    }

    @Test
    public void addCourseSameTypesInUnusedTimeSlot() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/11/15", df);
        LocalDate secondDate = LocalDate.parse("2016/11/25", df);

        Course course1 = testCourseBuilder().date(firstDate).duration(5).courseCode("AAA").build();
        Course course2 = testCourseBuilder().date(secondDate).duration(5).courseCode("AAA").build();

        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(course1)));
        when(impo.getCoursesFromFile("")).thenReturn(new ArrayList<>(Arrays.asList(course2)));

        assertThat(controller.addCourses(""), is(true));
    }

    @Test
    public void addCourseOverlapSameTypesTimeSlots() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/11/15", df);
        LocalDate secondDate = LocalDate.parse("2016/11/22", df);
        LocalDate thirdDate = LocalDate.parse("2016/11/18", df);

        Course course1 = testCourseBuilder().date(firstDate).courseCode("AAA").build();
        Course course2 = testCourseBuilder().date(secondDate).courseCode("AAA").build();
        Course course3 = testCourseBuilder().date(thirdDate).courseCode("AAA").build();

        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(course1, course2)));
        when(impo.getCoursesFromFile("")).thenReturn(new ArrayList<>(Arrays.asList(course3)));

        assertThat(controller.addCourses(""), is(false));
    }

    @Test
    public void addCourseOverlapDifferentTypes() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/11/15", df);
        LocalDate secondDate = LocalDate.parse("2016/11/15", df);

        Course course1 = testCourseBuilder().date(firstDate).duration(5).courseCode("AAA").build();
        Course course2 = testCourseBuilder().date(secondDate).duration(5).courseCode("BBB").build();

        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(course1)));
        when(impo.getCoursesFromFile("")).thenReturn(new ArrayList<>(Arrays.asList(course2)));

        assertThat(controller.addCourses(""), is(true));
    }

}
