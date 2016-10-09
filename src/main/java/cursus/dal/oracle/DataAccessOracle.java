package cursus.dal.oracle;

import cursus.dal.DataAccess;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;

/**
 * Created by maart on 9-10-2016.
 */

@Data
@NoArgsConstructor
public class DataAccessOracle implements DataAccess {

    private Connection conn;

    public void openConnection() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "cursus", "admin");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String test1() {
        String result = "";
        try {
            openConnection();
            int i = 1;
            String query = "SELECT * FROM CURSUS WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(i++,0);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                result = resultSet.getString("TEKSTJE");
            }

            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            closeConnection();
        }
        return result;
    }
}
