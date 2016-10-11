package cursus.dal.courseimport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by maart on 11-10-2016.
 */
public class CourseFileReader {

    public String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
