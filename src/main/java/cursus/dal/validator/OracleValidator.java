package cursus.dal.validator;


import cursus.dal.repositories.IRepository;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.Student;

import java.sql.SQLException;

/**
 * Created by maart on 10-10-2016.
 */
public class OracleValidator {

    IRepository repository = new RepositoryOracle();

    public Student getStudent(String id){
        try {
            return repository.getStudent(Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
