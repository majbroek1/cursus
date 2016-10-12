package cursus.controller;

import cursus.dal.courseimport.CourseImporter;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.Company;
import cursus.domain.Course;
import cursus.domain.Registration;
import cursus.domain.Student;
import org.junit.Before;
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
import static cursus.TestBuilders.testStudentBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by maart on 12-10-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerCourseTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RepositoryOracle repo;

    @Mock
    private CourseImporter impo;

    @InjectMocks
    private Controller controller;



    @Test
    public void getInvoicesForWeekNumberNull() throws SQLException {
        thrown.expect(NumberFormatException.class);
        controller.getInvoicesForWeekNumber(null);
    }

    @Test
    public void getInvoicesForWeekNumberInvalid() throws SQLException {
        thrown.expect(NumberFormatException.class);
        controller.getInvoicesForWeekNumber("AAA");
    }

    @Test
    public void getInvoicesForWeekNumberNormalBusiness() throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/10/10", df);
        LocalDate secondDate = LocalDate.parse("2016/10/12", df);
        LocalDate thirdDate = LocalDate.parse("2016/10/14", df);

        Course course1 = testCourseBuilder().date(firstDate).courseCode("AAA").build();
        Course course2 = testCourseBuilder().date(secondDate).courseCode("BBB").build();
        Course course3 = testCourseBuilder().date(thirdDate).courseCode("CCC").build();

        Company company = Company.builder().name("Company 1").accountNumber("1234").build();

        Student student1 = testStudentBuilder().company(company).name("Studentname1").accountNumber("4321").build();
        Student student2 = testStudentBuilder().company(company).name("Studentname2").accountNumber("9876").build();

        Registration registration1 = Registration.builder().student(student1).course(course1).business(true).build();
        Registration registration2 = Registration.builder().student(student1).course(course2).business(true).build();
        Registration registration3 = Registration.builder().student(student2).course(course3).business(true).build();
        Registration registration4 = Registration.builder().student(student2).course(course1).business(true).build();

        ArrayList<Registration> registrations = new ArrayList<>(Arrays.asList(registration1, registration2, registration3, registration4));

        when(repo.getAllRegistrations()).thenReturn(registrations);

        assertThat(controller.getInvoicesForWeekNumber("41").size(), is(4));
    }

    @Test
    public void getInvoicesForWeekNumberNormalPrivate() throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate firstDate = LocalDate.parse("2016/10/10", df);
        LocalDate secondDate = LocalDate.parse("2016/10/12", df);
        LocalDate thirdDate = LocalDate.parse("2016/10/14", df);

        Course course1 = testCourseBuilder().date(firstDate).courseCode("AAA").build();
        Course course2 = testCourseBuilder().date(secondDate).courseCode("BBB").build();
        Course course3 = testCourseBuilder().date(thirdDate).courseCode("CCC").build();

        Company company = Company.builder().name("Company 1").accountNumber("1234").build();

        Student student1 = testStudentBuilder().company(company).name("Studentname1").accountNumber("4321").build();
        Student student2 = testStudentBuilder().company(company).name("Studentname2").accountNumber("9876").build();

        Registration registration1 = Registration.builder().student(student1).course(course1).business(false).build();
        Registration registration2 = Registration.builder().student(student1).course(course2).business(false).build();
        Registration registration3 = Registration.builder().student(student2).course(course3).business(false).build();

        ArrayList<Registration> registrations = new ArrayList<>(Arrays.asList(registration1, registration2, registration3));

        when(repo.getAllRegistrations()).thenReturn(registrations);

        assertThat(controller.getInvoicesForWeekNumber("41").size(), is(3));
    }
}
