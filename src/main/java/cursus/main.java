package cursus;

import cursus.dal.courseimport.CourseImporter;

/**
 * Created by maart on 7-10-2016.
 */
public class main {

    public static void main(String[] args) throws Exception {

        CourseImporter reader = new CourseImporter();

        reader.getCoursesFromFile("D:/coursetest.txt");

//        IRepository data = new RepositoryOracle();

//        Course course =  Course.builder().date(LocalDate.now())
//                .courseCode("aa")
//                .name("bb")
//                .duration(1)
//                .build();
//
//        Company company = Company.builder().id(0).build();
//        Student student = Student.builder().company(company).name("aa").build();
//
//        System.out.println(data.addCourse(course));
//        System.out.println(data.getCourse(1).getName());
//        System.out.println(data.getAllCourses().size());
//        System.out.println(data.getCompany(1).getName());
//        System.out.println(data.getStudent(8).getName());
//        System.out.println(data.addStudent(student));
    }
}
