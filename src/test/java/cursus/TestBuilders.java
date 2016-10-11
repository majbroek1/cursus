package cursus;

import cursus.domain.Student;

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

}
