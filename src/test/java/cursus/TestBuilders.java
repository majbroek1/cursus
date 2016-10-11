package cursus;

import cursus.domain.Course;
import cursus.domain.Student;

import java.time.LocalDate;

/**
 * Created by maart on 11-10-2016.
 */
public class TestBuilders {

    public static Student.StudentBuilder testStudentBuilder(){
        return Student.builder().id(1)
                .name("studentname")
                .lastName("studentlastname")
                .address("studentaddress")
                .email("studentemail");
    }

    public static Course.CourseBuilder testCourseBuilder(){
        return Course.builder().id(1)
                .name("coursename")
                .date(LocalDate.now())
                .duration(5)
                .courseCode("ACSX");
    }

}
