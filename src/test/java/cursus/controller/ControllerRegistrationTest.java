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
import java.util.ArrayList;

import static cursus.TestBuilders.testCourseBuilder;
import static cursus.TestBuilders.testStudentBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by maart on 12-10-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerRegistrationTest {

    private Company company;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RepositoryOracle repo;

    @Mock
    private CourseImporter impo;

    @InjectMocks
    private Controller controller;

    @Before
    public void Init(){
        company = Company.builder().id(1)
                .name("companyname")
                .address("companyaddress")
                .accountNumber("123456789")
                .email("company@company.com")
                .build();
    }

    @Test
    public void addRegistrationNoValidStudentAndCourse() throws SQLException {
        Student student = testStudentBuilder().id(-1).build();
        Course course = testCourseBuilder().id(-1).build();
        Registration registration = new Registration(course, student, false);
        when(repo.getCourse(-1)).thenReturn(null);
        when(repo.getStudentById(-1)).thenReturn(null);
        when(repo.addRegistration(registration)).thenReturn(false);


        assertThat(controller.addRegistration(registration), is(false));
    }

    @Test
    public void addRegistrationNoValidStudent() throws SQLException {
        Student student = testStudentBuilder().id(-1).build();
        Course course = testCourseBuilder().id(1).build();
        Registration registration = new Registration(course, student, false);
        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(-1)).thenReturn(null);

        assertThat(controller.addRegistration(registration), is(false));
    }

    @Test
    public void addRegistrationNoValidCourse() throws SQLException {
        Student student = testStudentBuilder().id(1).build();
        Course course = testCourseBuilder().id(-1).build();
        Registration registration = new Registration(course, student, false);
        when(repo.getCourse(-1)).thenReturn(null);
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());

        assertThat(controller.addRegistration(registration), is(false));
    }

    @Test
    public void addRegistrationNullCompany() throws SQLException {
        Student student = testStudentBuilder().id(1).company(null).build();
        Course course = testCourseBuilder().id(1).build();
        Registration registration = new Registration(course, student, false);
        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());
        when(repo.addRegistration(registration)).thenReturn(true);

        assertThat(controller.addRegistration(registration), is(true));
    }

    @Test
    public void addRegistration() throws SQLException {
        Student student = testStudentBuilder().build();
        Course course = testCourseBuilder().build();
        Registration registration = new Registration(course, student, false);

        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());
        when(repo.addRegistration(registration)).thenReturn(true);

        assertThat(controller.addRegistration(registration), is(true));
    }

    @Test
    public void addRegistrationForCompanyHas() throws SQLException {
        Student student = testStudentBuilder().company(company).build();
        Course course = testCourseBuilder().build();
        Registration registration = new Registration(course, student, true);

        when(repo.getCompany(1)).thenReturn(company);
        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());
        when(repo.addRegistration(registration)).thenReturn(true);

        assertThat(controller.addRegistration(registration), is(true));
    }

    @Test
    public void addRegistrationForCompanyInvalid() throws SQLException {
        Student student = testStudentBuilder().company(company).build();
        Course course = testCourseBuilder().build();
        Registration registration = new Registration(course, student, true);

        when(repo.getCompany(1)).thenReturn(null);
        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());
        when(repo.addRegistration(registration)).thenReturn(true);

        assertThat(controller.addRegistration(registration), is(true));
    }

}
