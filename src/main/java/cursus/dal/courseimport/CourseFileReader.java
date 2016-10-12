package cursus.dal.courseimport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by maart on 11-10-2016.
 */
public class CourseFileReader {

    public String readFile(String fileName) throws IOException {
        if (fileExists(fileName)) {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } else {
            throw new IOException("No file found");
        }
    }

    public boolean fileExists(String fileName) {
        File f = new File(fileName);
        if (f.exists()) {
            return true;
        } else {
            return false;
        }
    }

}
