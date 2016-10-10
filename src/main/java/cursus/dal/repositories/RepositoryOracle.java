package cursus.dal.repositories;

import cursus.domain.Company;
import cursus.domain.Course;
import cursus.domain.Student;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by maart on 9-10-2016.
 */

@Data
@NoArgsConstructor
public class RepositoryOracle implements IRepository {

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

    public Course getCourse(int courseId) throws SQLException {
        Course course = null;
        try {
            openConnection();
            String query = "SELECT * FROM COURSE WHERE courseid = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            int i = 1;
            stmt.setInt(i++, courseId);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                course = Course.builder().id(resultSet.getInt("courseid"))
                        .courseCode(resultSet.getString("coursecode"))
                        .name(resultSet.getString("coursename"))
                        .date(resultSet.getDate("coursedate").toLocalDate())
                        .duration(resultSet.getInt("duration"))
                        .build();
            }
            resultSet.close();
            stmt.close();
        } finally {
            closeConnection();
        }
        return course;
    }

    @Override
    public ArrayList<Student> getAllStudents() throws SQLException {
        ArrayList<Student> result = new ArrayList<>();
        try {
            openConnection();

            String query = "SELECT * FROM STUDENT";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Student student = Student.builder().id(resultSet.getInt("studentid"))
                        .company(getCompany(resultSet.getInt("companyid")))
                        .name(resultSet.getString("studentname"))
                        .lastName(resultSet.getString("lastname"))
                        .email(resultSet.getString("email"))
                        .accountNumber(resultSet.getString("accountnumber"))
                        .address(resultSet.getString("address"))
                        .build();
                result.add(student);
            }
            resultSet.close();
            stmt.close();
        } finally {
            closeConnection();
        }
        return result;
    }

    public ArrayList<Course> getAllCourses() throws SQLException {
        ArrayList<Course> result = new ArrayList<>();
        try {
            openConnection();

            String query = "SELECT * FROM COURSE";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Course course = Course.builder().id(resultSet.getInt("courseid"))
                        .courseCode(resultSet.getString("coursecode"))
                        .name(resultSet.getString("coursename"))
                        .date(resultSet.getDate("coursedate").toLocalDate())
                        .duration(resultSet.getInt("duration"))
                        .build();
                result.add(course);
            }
            resultSet.close();
            stmt.close();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public boolean addStudent(Student student) throws SQLException {
        boolean result = false;
        try {
            openConnection();

            String query = "INSERT INTO STUDENT(COMPANYID,STUDENTNAME,LASTNAME,EMAIL,ACCOUNTNUMBER,ADDRESS) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            int i = 1;
            stmt.setInt(i++, student.getCompany().getId());
            stmt.setString(i++,student.getName());
            stmt.setString(i++,student.getLastName());
            stmt.setString(i++,student.getEmail());
            stmt.setString(i++,student.getAccountNumber());
            stmt.setString(i++,student.getAddress());

            int numberOfRows = stmt.executeUpdate();

            if (numberOfRows <= 0) {
                result = false;
            } else {
                result = true;
            }
            stmt.close();
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean addCourse(Course course) throws SQLException {
        boolean result = false;
        try {
            openConnection();

            String query = "INSERT INTO COURSE(coursecode,coursename,coursedate,duration) VALUES (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            int i = 1;
            stmt.setString(i++, course.getCourseCode());
            stmt.setString(i++, course.getName());
            stmt.setDate(i++, Date.valueOf(course.getDate()));
            stmt.setInt(i++, course.getDuration());

            int numberOfRows = stmt.executeUpdate();

            if (numberOfRows <= 0) {
                result = false;
            } else {
                result = true;
            }
            stmt.close();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public Student getStudent(int studentId) throws SQLException {
        Student student = null;
        try {
            openConnection();
            String query = "SELECT * FROM STUDENT WHERE studentid = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            int i = 1;
            stmt.setInt(i++, studentId);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                student = Student.builder().id(resultSet.getInt("studentid"))
                        .company(getCompany(resultSet.getInt("companyid")))
                        .name(resultSet.getString("studentname"))
                        .lastName(resultSet.getString("lastname"))
                        .email(resultSet.getString("email"))
                        .accountNumber(resultSet.getString("accountnumber"))
                        .address(resultSet.getString("address"))
                        .build();
            }
            resultSet.close();
            stmt.close();
        } finally {
            closeConnection();
        }
        return student;
    }

    @Override
    public Company getCompany(int companyId) throws SQLException {
        Company company = null;
        try {
            openConnection();
            String query = "SELECT * FROM COMPANY WHERE companyid = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            int i = 1;
            stmt.setInt(i++, companyId);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                company = Company.builder().id(resultSet.getInt("companyid"))
                        .accountNumber(resultSet.getString("accountnumber"))
                        .name(resultSet.getString("companyname"))
                        .address(resultSet.getString("address"))
                        .build();
            }
            resultSet.close();
            stmt.close();
        } finally {
            closeConnection();
        }
        return company;
    }


}
