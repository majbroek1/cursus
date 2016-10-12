package cursus;

import cursus.dal.courseimport.CourseImporter;
import cursus.dal.repositories.IRepository;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.Company;
import cursus.domain.Course;
import cursus.domain.Registration;
import cursus.domain.Student;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by maart on 7-10-2016.
 */
public class main {

    public static void main(String[] args) throws Exception {


        IRepository data = new RepositoryOracle();

        Course course =  Course.builder().date(LocalDate.now())
                .courseCode("aa")
                .name("bb")
                .duration(1)
                .build();

        Course course2 =  Course.builder().date(LocalDate.now())
                .id(1)
                .courseCode("aa")
                .name("bb")
                .duration(1)
                .build();

        Company company = Company.builder().id(0).build();
        Student student = Student.builder().company(company).name("aa").build();
        Registration registration = Registration.builder().course(course2).student(Student.builder().id(8).build()).business(false).build();


//        ArrayList<Registration> registrations = data.getAllRegistrations();
//        System.out.println(data.getAllRegistrations().size());
//        System.out.println(data.addCourse(course));
//        System.out.println(data.getCourse(1).getName());
//        System.out.println(data.getAllCourses().size());
//        System.out.println(data.getCompany(1).getName());
//        System.out.println(data.getStudentById(8).getName());
//        System.out.println(data.addStudent(student));
        System.out.println(data.addRegistration(registration));

    }
}
