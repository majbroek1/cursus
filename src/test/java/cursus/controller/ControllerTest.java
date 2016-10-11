package cursus.controller;

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

import static cursus.TestBuilders.testStudentBuilder;
import static cursus.TestBuilders.testCourseBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

/**
 * Created by maart on 11-10-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    Company company;
    Company invalidCompany;
    Student businessStudent;
    Student privateStudent;
    ArrayList<Student> threeStudents;
    ArrayList<Student> zeroStudents;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RepositoryOracle repo;

    @InjectMocks
    private Controller controller;

    @Before
    public void init(){
        company = Company.builder().id(1)
                .name("companyname")
                .address("companyaddress")
                .accountNumber("123456789")
                .email("company@company.com")
                .build();
        invalidCompany = Company.builder().id(5)
                .name("companyname")
                .address("companyaddress")
                .accountNumber("123456789")
                .email("company@company.com")
                .build();
        businessStudent = testStudentBuilder().company(company).build();
        privateStudent = testStudentBuilder().build();
        threeStudents = new ArrayList<>();
        threeStudents.add(businessStudent);
        threeStudents.add(businessStudent);
        threeStudents.add(businessStudent);
        zeroStudents = new ArrayList<>();
    }


    @Test
    public void getThreeStudentsByEmail() throws SQLException {
        when(repo.getStudentsByEmail("mail")).thenReturn(threeStudents);

        assertThat(controller.getStudentsByEmail("mail").size(),is(3));
    }

    @Test
    public void getZeroStudentsByEmail() throws SQLException{
        when(repo.getStudentsByEmail("")).thenReturn(zeroStudents);

        assertThat(controller.getStudentsByEmail("").size(),is(0));
    }

    @Test
    public void getStudentByEmailException() throws SQLException{
        thrown.expect(SQLException.class);
        when(repo.getStudentsByEmail("")).thenThrow(new SQLException("error"));

        controller.getStudentsByEmail("");
    }

    @Test
    public void getStudentByIdNull() throws SQLException {
        thrown.expect(NumberFormatException.class);

        controller.getStudentById(null);
    }

    @Test
    public void getStudentByIdText() throws SQLException{
        thrown.expect(NumberFormatException.class);

        controller.getStudentById("henk");
    }

    @Test
    public void getStudentById() throws SQLException {
        when(repo.getStudentById(1)).thenReturn(businessStudent);

        assertThat(controller.getStudentById("1").getName(),is("studentname"));
    }

    @Test
    public void getStudentByIdNone() throws SQLException {
        when(repo.getStudentById(123)).thenReturn(null);

        assertThat(controller.getStudentById("123"), nullValue());
    }

    @Test
    public void addStudentWithCompany() throws SQLException {
        when(repo.getCompany(1)).thenReturn(company);
        when(repo.addStudent(businessStudent)).thenReturn(true);

        assertThat(controller.addStudent(businessStudent),is(true));
    }

    @Test
    public void addStudentNoBusinessReference() throws SQLException {
        when(repo.getCompany(0)).thenReturn(company);
        when(repo.addStudent(privateStudent)).thenReturn(true);

        assertThat(controller.addStudent(privateStudent),is(true));
    }

    @Test
    public void addStudentInvalidBusinessReference() throws SQLException {
        Student invalidCompanyStudent = testStudentBuilder().company(invalidCompany).build();

        when(repo.getCompany(5)).thenReturn(null);
        when(repo.addStudent(invalidCompanyStudent)).thenReturn(true);

        assertThat(controller.addStudent(invalidCompanyStudent),is(false));
    }

    @Test
    public void addRegistrationNoValidStudentAndCourse() throws SQLException {
        Student student = testStudentBuilder().id(-1).build();
        Course course = testCourseBuilder().id(-1).build();
        Registration registration = new Registration(course,student,false);
        when(repo.getCourse(-1)).thenReturn(null);
        when(repo.getStudentById(-1)).thenReturn(null);
        when(repo.addRegistration(registration)).thenReturn(false);


        assertThat(controller.addRegistration(registration), is(false));
    }

    @Test
    public void addRegistrationNoValidStudent() throws SQLException {
        Student student = testStudentBuilder().id(-1).build();
        Course course = testCourseBuilder().id(1).build();
        Registration registration = new Registration(course,student,false);
        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(-1)).thenReturn(null);

        assertThat(controller.addRegistration(registration), is(false));
    }

    @Test
    public void addRegistrationNoValidCourse() throws SQLException {
        Student student = testStudentBuilder().id(1).build();
        Course course = testCourseBuilder().id(-1).build();
        Registration registration = new Registration(course,student,false);
        when(repo.getCourse(-1)).thenReturn(null);
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());

        assertThat(controller.addRegistration(registration), is(false));
    }

    @Test
    public void addRegistrationNullCompany() throws SQLException {
        Student student = testStudentBuilder().id(1).company(null).build();
        Course course = testCourseBuilder().id(1).build();
        Registration registration = new Registration(course,student,false);
        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());
        when(repo.addRegistration(registration)).thenReturn(true);

        assertThat(controller.addRegistration(registration), is(true));
    }

    @Test
    public void addRegistration() throws SQLException {
        Student student = testStudentBuilder().build();
        Course course = testCourseBuilder().build();
        Registration registration = new Registration(course,student,false);

        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());
        when(repo.addRegistration(registration)).thenReturn(true);

        assertThat(controller.addRegistration(registration), is(true));
    }

    @Test
    public void addRegistrationForCompanyHas() throws SQLException {
        Student student = testStudentBuilder().company(company).build();
        Course course = testCourseBuilder().build();
        Registration registration = new Registration(course,student,true);

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
        Registration registration = new Registration(course,student,true);

        when(repo.getCompany(1)).thenReturn(null);
        when(repo.getCourse(1)).thenReturn(testCourseBuilder().build());
        when(repo.getStudentById(1)).thenReturn(testStudentBuilder().build());
        when(repo.addRegistration(registration)).thenReturn(true);

        assertThat(controller.addRegistration(registration), is(true));
    }

    @Test
    public void getStudentsFromCompany() throws SQLException{
        when(repo.getAllStudentsFromCompany(1)).thenReturn(threeStudents);

        assertThat(controller.getStudentsByCompany("1").size(),is(3));
    }

    @Test
    public void getStudentsFromCompanyNone() throws SQLException {
        when(repo.getAllStudentsFromCompany(1)).thenReturn(new ArrayList<Student>());

        assertThat(controller.getStudentsByCompany("1").size(),is(0));
    }

    @Test
    public void getStudentsFromCompanyNull() throws SQLException {
        thrown.expect(NumberFormatException.class);

        controller.getStudentsByCompany(null);
    }

    @Test
    public void getStudentsFromCompanyWrongValue() throws SQLException {
        thrown.expect(NumberFormatException.class);

        controller.getStudentsByCompany("abc");
    }

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
        LocalDate dateGood = LocalDate.parse("2016/10/15",df);
        LocalDate dateBad = LocalDate.parse("2016/11/15",df);

        Course goodCourse = Course.builder().date(dateGood).build();
        Course badCourse = Course.builder().date(dateBad).build();
        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(goodCourse,goodCourse,badCourse)));

        assertThat(controller.getCoursesFromWeek("41").size(),is(2));
    }

    @Test
    public void getNoCoursesFromWeek() throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateBad = LocalDate.parse("2016/11/15",df);

        Course badCourse = Course.builder().date(dateBad).build();
        when(repo.getAllCourses()).thenReturn(new ArrayList<>(Arrays.asList(badCourse,badCourse,badCourse)));

        assertThat(controller.getCoursesFromWeek("41").size(),is(0));
    }




}