package cursus.dal.repositories;

import cursus.domain.Company;
import cursus.domain.Course;
import cursus.domain.Student;

import java.util.ArrayList;

/**
 * Created by maart on 9-10-2016.
 */
public interface IRepository {

    Course getCourse(int courseId);

    ArrayList<Course> getAllCourses();

    boolean addCourse(Course course);

    Student getStudent(int studentId);

    ArrayList<Student> getAllStudents();

    boolean addStudent(Student student);

    Company getCompany(int companyId);


}
