package cursus.controller;


import cursus.dal.courseimport.CourseImporter;
import cursus.dal.courseimport.ICourseImporter;
import cursus.dal.repositories.IRepository;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
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
                .filter(course -> getWeekNumber(course.getDate()) == weekNumber)
                .collect(Collectors.toCollection(() -> new ArrayList<Course>()));
        return result;
    }

    public boolean addCourses(String wholeFile) throws Exception {
        boolean result = false;
        ArrayList<Course> newCourses = importer.getCoursesFromFile(wholeFile);
        for (Course newCourse: newCourses){
            ArrayList<Course> oldCourses = repository.getAllCourses();
            boolean taken = false;
            for (Course oldCourse: oldCourses){
                if ((newCourse.getDate().getDayOfYear() >= oldCourse.getDate().getDayOfYear() &&
                        newCourse.getDate().getDayOfYear() <= oldCourse.getEndDate().getDayOfYear()
                        ) || (
                        newCourse.getEndDate().getDayOfYear() <= oldCourse.getEndDate().getDayOfYear() &&
                        newCourse.getEndDate().getDayOfYear() >= oldCourse.getDate().getDayOfYear())){
                    if (newCourse.getCourseCode().equalsIgnoreCase(oldCourse.getCourseCode())){
                        taken = true;
                    }
                }
            }
            if (!taken){
                repository.addCourse(newCourse);
                result = true;
            }
        }
        return result;
    }

    private int getWeekNumber(LocalDate date){
        //Germany ipv default. Lokaal werkt het, build server niet. NL is er niet, maar Germany ligt het dichtst bij wat voor deze case overeen komt met NL.
        return date.get(WeekFields.of(Locale.GERMANY).weekOfYear());
    }

    public ArrayList<Invoice> getInvoicesForWeekNumber(String week) throws SQLException {
        int weekNumber = Integer.parseInt(week);
        ArrayList<Registration> registrations = repository.getAllRegistrations().stream()
                .filter(reg -> getWeekNumber(reg.getCourse().getDate()) == weekNumber)
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
        ArrayList<Invoice> invoices = new ArrayList<>();

        for (Registration reg: registrations){
            if (reg.isBusiness()){
                Invoice invoice = Invoice.builder().name(reg.getStudent().getName())
                        .companyName(reg.getStudent().getCompany().getName())
                        .accountNumber(reg.getStudent().getCompany().getAccountNumber())
                        .email(reg.getStudent().getCompany().getEmail())
                        .courseName(reg.getCourse().getName())
                        .courseCode(reg.getCourse().getCourseCode())
                        .build();
                invoices.add(invoice);
            }
            else{
                Invoice invoice = Invoice.builder().name(reg.getStudent().getName())
                        .companyName("N.V.T.")
                        .accountNumber(reg.getStudent().getAccountNumber())
                        .email(reg.getStudent().getEmail())
                        .courseName(reg.getCourse().getName())
                        .courseCode(reg.getCourse().getCourseCode())
                        .build();
                invoices.add(invoice);
            }
        }
        return invoices;
    }



}
