package cursus.dal.courseimport;

import cursus.domain.Course;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by maart on 11-10-2016.
 */
public class CourseImporter implements ICourseImporter {

    private CourseFileReader fileReader = new CourseFileReader();

    public ArrayList<Course> getCoursesFromFile(String fileName) throws Exception {
        String wholeFile = fileReader.readFile(fileName);
        ArrayList<String> courseBlocks = getCourseBlocks(wholeFile);
        ArrayList<Course> courses = new ArrayList<>();
        for (String course : courseBlocks) {
            ArrayList<String> courseLines = getSeperateLines(course);
            if (courseLines.size() == 4) {
                int successful = 0;
                Course.CourseBuilder courseBuild = Course.builder();
                ArrayList<Integer> positions = new ArrayList<>();
                for (String line : courseLines) {
                    if (line.startsWith("Titel: ")) {
                        courseBuild.name(line.substring(7, line.length()));
                        successful++;
                        positions.add(1);
                    }
                    if (line.startsWith("Cursuscode: ")) {
                        courseBuild.courseCode(line.substring(12, line.length()));
                        successful++;
                        positions.add(2);
                    }
                    if (line.startsWith("Duur: ")) {
                        String daysString = line.substring(6, line.length());
                        if (daysString.length() > 2) {
                            courseBuild.duration(Integer.parseInt(daysString.substring(0, 1)));
                            successful++;
                            positions.add(3);
                        } else {
                            System.out.println("word dagen is missing");
                        }
                    }
                    if (line.startsWith("Startdatum: ")) {
                        String givenDate = line.substring(12, line.length());
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate date = LocalDate.parse(givenDate, df);
                        courseBuild.date(date);
                        successful++;
                        positions.add(4);
                    }
                }
                if (successful == 4) {
                    if (isCorrectOrder(positions)) {
                        courses.add(courseBuild.build());
                    }
                    else{
                        System.out.println("wrong field order");
                    }
                }
            } else {
                System.out.println("Wrong file format");
            }
        }
        return courses;
    }

    private ArrayList<String> getCourseBlocks(String wholeDocument) {
        String[] courseBlocks = wholeDocument.split("\\r\\n\\r\\n");
        return new ArrayList<>(Arrays.asList(courseBlocks));
    }

    private ArrayList<String> getSeperateLines(String courseBlock) {
        String[] seperateLines = courseBlock.split("\\r\\n");
        return new ArrayList<>(Arrays.asList(seperateLines));
    }

    private boolean isCorrectOrder(ArrayList<Integer> positions) {
        int lastNumber = 0;
        for (int i : positions) {
            if (i > lastNumber) {
                lastNumber = i;
            } else {
                return false;
            }
        }
        return true;
    }


}
