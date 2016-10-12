package cursus.dal.courseimport;

import cursus.domain.Course;

import java.util.ArrayList;

/**
 * Created by maart on 11-10-2016.
 */
public interface ICourseImporter {

    ArrayList<Course> getCoursesFromFile(String wholeFile) throws Exception;
}
