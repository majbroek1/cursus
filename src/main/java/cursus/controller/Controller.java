package cursus.controller;


import cursus.dal.repositories.IRepository;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.Student;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by maart on 10-10-2016.
 */
public class Controller {

    IRepository repository = new RepositoryOracle();

    public Student getStudentById(String id) throws SQLException {
        return repository.getStudentById(Integer.parseInt(id));
    }

    public ArrayList<Student> getStudentsByEmail(String email) throws SQLException {
        return repository.getStudentsByEmail(email);
    }


    public boolean addStudent(Student student) throws SQLException {
        return repository.addStudent(student);
    }
}
