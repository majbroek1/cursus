package cursus.controller;

import cursus.dal.courseimport.CourseImporter;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.*;
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
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.contains;
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

    @Mock
    private CourseImporter impo;

    @InjectMocks
    private Controller controller;

    @Before
    public void init() {
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

        assertThat(controller.getStudentsByEmail("mail").size(), is(3));
    }

    @Test
    public void getZeroStudentsByEmail() throws SQLException {
        when(repo.getStudentsByEmail("")).thenReturn(zeroStudents);

        assertThat(controller.getStudentsByEmail("").size(), is(0));
    }

    @Test
    public void getStudentByEmailException() throws SQLException {
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
    public void getStudentByIdText() throws SQLException {
        thrown.expect(NumberFormatException.class);

        controller.getStudentById("henk");
    }

    @Test
    public void getStudentById() throws SQLException {
        when(repo.getStudentById(1)).thenReturn(businessStudent);

        assertThat(controller.getStudentById("1").getName(), is("studentname"));
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

        assertThat(controller.addStudent(businessStudent), is(true));
    }

    @Test
    public void addStudentNoBusinessReference() throws SQLException {
        when(repo.getCompany(0)).thenReturn(company);
        when(repo.addStudent(privateStudent)).thenReturn(true);

        assertThat(controller.addStudent(privateStudent), is(true));
    }

    @Test
    public void addStudentInvalidBusinessReference() throws SQLException {
        Student invalidCompanyStudent = testStudentBuilder().company(invalidCompany).build();

        when(repo.getCompany(5)).thenReturn(null);
        when(repo.addStudent(invalidCompanyStudent)).thenReturn(true);

        assertThat(controller.addStudent(invalidCompanyStudent), is(false));
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

    @Test
    public void getStudentsFromCompany() throws SQLException {
        when(repo.getAllStudentsFromCompany(1)).thenReturn(threeStudents);

        assertThat(controller.getStudentsByCompany("1").size(), is(3));
    }

    @Test
    public void getStudentsFromCompanyNone() throws SQLException {
        when(repo.getAllStudentsFromCompany(1)).thenReturn(new ArrayList<Student>());

        assertThat(controller.getStudentsByCompany("1").size(), is(0));
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

        ArrayList<Registration> registrations = new ArrayList<>(Arrays.asList(registration1,registration2,registration3,registration4));

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

        ArrayList<Registration> registrations = new ArrayList<>(Arrays.asList(registration1,registration2,registration3));

        when(repo.getAllRegistrations()).thenReturn(registrations);

        assertThat(controller.getInvoicesForWeekNumber("41").size(), is(3));
    }





}