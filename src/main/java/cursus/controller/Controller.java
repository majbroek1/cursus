package cursus.controller;


import cursus.dal.courseimport.CourseImporter;
import cursus.dal.courseimport.ICourseImporter;
import cursus.dal.repositories.IRepository;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.Company;
import cursus.domain.Course;
import cursus.domain.Registration;
import cursus.domain.Student;

import java.sql.SQLException;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by maart on 10-10-2016.
 */
public class Controller {

    IRepository repository = new RepositoryOracle();
    ICourseImporter importer = new CourseImporter();

    public Student getStudentById(String id) throws SQLException {
        return repository.getStudentById(Integer.parseInt(id));
    }

    public ArrayList<Student> getStudentsByEmail(String email) throws SQLException {
        return repository.getStudentsByEmail(email);
    }

    public ArrayList<Student> getStudentsByCompany(String id) throws SQLException{
        return repository.getAllStudentsFromCompany(Integer.parseInt(id));
    }

    public boolean addStudent(Student student) throws SQLException {
        if (student.getCompany() == null) {
            Company company = Company.builder().id(0).build();
            student.setCompany(company);
        }
        Company company = repository.getCompany(student.getCompany().getId());
        if (company != null) {
            return repository.addStudent(student);
        } else {
            return false;
        }
    }

    public boolean addRegistration(Registration registration) throws SQLException {
        if (registration.getStudent().getCompany() == null) {
            Company company = Company.builder().id(0).build();
            registration.getStudent().setCompany(company);
        }
        Company company = repository.getCompany(registration.getStudent().getCompany().getId());
        if (registration.isBusiness() == true) {
            //register
            if (company != null){
                return repository.addRegistration(registration);
            }else{
                registration.setBusiness(false);
                return repository.addRegistration(registration);
            }
        }
        else{
            return repository.addRegistration(registration);
        }
    }








    public ArrayList<Course> getCoursesFromWeek(String week) throws SQLException {
        int weekNumber = Integer.parseInt(week);
        ArrayList<Course> allCourses = repository.getAllCourses();
        ArrayList<Course> result = allCourses.stream()
                .filter(course -> course.getDate().get(WeekFields.of(Locale.getDefault()).weekOfYear()) == weekNumber)
                .collect(Collectors.toCollection(() -> new ArrayList<Course>()));
        return result;
    }
}
