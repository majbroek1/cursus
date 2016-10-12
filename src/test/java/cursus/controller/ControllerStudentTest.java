package cursus.controller;

import cursus.dal.courseimport.CourseImporter;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.Company;
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

import static cursus.TestBuilders.testStudentBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by maart on 12-10-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerStudentTest {

    private Company company;
    private Company invalidCompany;
    private Student businessStudent;
    private Student privateStudent;
    private ArrayList<Student> threeStudents;
    private ArrayList<Student> zeroStudents;

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
        invalidCompany = Company.builder().id(5)
                .name("companyname")
                .address("companyaddress")
                .accountNumber("123456789")
                .email("company@company.com")
                .build();
        privateStudent = testStudentBuilder().build();
        businessStudent = testStudentBuilder().company(company).build();
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

}
