package cursus.dal.repositories;

import cursus.domain.Company;
import cursus.domain.Course;
import cursus.domain.Registration;
import cursus.domain.Student;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by maart on 9-10-2016.
 */
public interface IRepository {

    Course getCourse(int courseId) throws SQLException;

    ArrayList<Course> getAllCourses() throws SQLException;

    boolean addCourse(Course course) throws SQLException;

    Student getStudentById(int studentId) throws SQLException;

    ArrayList<Student> getStudentsByEmail(String studentMail) throws SQLException;

    ArrayList<Student> getAllStudents() throws SQLException;

    boolean addStudent(Student student) throws SQLException;

    Company getCompany(int companyId) throws SQLException;

    boolean addRegistration(Registration registration) throws SQLException;

    ArrayList<Registration> getAllRegistrations() throws SQLException;

    ArrayList<Student> getAllStudentsFromCompany(int companyId) throws SQLException;



}
