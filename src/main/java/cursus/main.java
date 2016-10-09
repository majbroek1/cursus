package cursus;

import cursus.dal.DataAccess;
import cursus.dal.oracle.DataAccessOracle;

/**
 * Created by maart on 7-10-2016.
 */
public class main {

    public static void main(String[] args) {

        DataAccess data = new DataAccessOracle();

        System.out.println(data.test1());

    }
}
