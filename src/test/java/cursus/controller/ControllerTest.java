package cursus.controller;

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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by maart on 11-10-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    Company company;
    Student student;
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
        student = Student.builder().id(1)
                .company(company)
                .name("studentname")
                .lastName("studentlastname")
                .address("studentaddress")
                .email("studentemail")
                .build();
        threeStudents = new ArrayList<>();
        threeStudents.add(student);
        threeStudents.add(student);
        threeStudents.add(student);
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
        when(repo.getStudentById(1)).thenReturn(student);

        assertThat(controller.getStudentById("1").getName(),is("studentname"));
    }

    @Test
    public void getStudentByIdNone() throws SQLException {
        when(repo.getStudentById(123)).thenReturn(null);

        assertThat(controller.getStudentById("123"), nullValue());
    }

    @Test
    public void addStudent() throws SQLException {
        when(repo.addStudent(student)).thenReturn(true);

        assertThat(controller.addStudent(student),is(true));
    }


}