package cursus.dal.courseimport;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by maart on 11-10-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CourseImporterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private CourseFileReader reader;

    @InjectMocks
    CourseImporter importer = new CourseImporter();

    @Test
    public void emptyFile() throws Exception {
        when(reader.readFile("")).thenReturn("");

        importer.getCoursesFromFile("");
        assertThat(importer.getCoursesFromFile("").size(),is(0));
    }

    @Test
    public void singleHalfCourse() throws Exception {
        String file = "Titel: C# Programmeren\r\n" +
                      "Cursuscode: CNETIN\r\n";

        assertThat(importer.getCoursesFromFile(file).size(),is(0));
    }

    @Test
    public void singleCourse() throws Exception {
        String file = "Titel: C# Programmeren\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 14/10/2013\r\n";

        assertThat(importer.getCoursesFromFile(file).size(),is(1));
    }

    @Test
    public void singleCourseDagenWrong() throws Exception {
        String file = "Titel: C# Programmeren\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Duur: 5\r\n" +
                "Startdatum: 14/10/2013\r\n";

        assertThat(importer.getCoursesFromFile(file).size(),is(0));
    }

    @Test
    public void singleCourseSwappedField() throws Exception {
        String file = "Titel: C# Programmeren\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Startdatum: 14/10/2013\r\n" +
                "Duur: 5 dagen\r\n" ;

        assertThat(importer.getCoursesFromFile(file).size(),is(0));
    }

    @Test
    public void twoCourses() throws Exception {
        String file = "Titel: C# Programmeren\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 14/10/2013\r\n" +
                "\r\n" +
                "Titel: C# Programmeren\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 21/10/2013";

        when (reader.readFile("")).thenReturn(file);

        assertThat(importer.getCoursesFromFile(file).size(), is(2));
    }

    @Test
    public void threeCoursesOneWrong() throws Exception {
        String file =
                "Titel: C# Programmeren\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 14/10/2013\r\n" +
                "\r\n" +
                "Titel: C# Programmeren\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 21/10/2013\r\n" +
                "\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Titel: C# Programmeren\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 21/10/2013";

        when (reader.readFile("")).thenReturn(file);

        assertThat(importer.getCoursesFromFile(file).size(), is(2));
    }

    @Test
    public void threeCoursesThreeWrong() throws Exception {
        String file =
                "Cursuscode: CNETIN\r\n" +
                "Titel: C# Programmeren\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 14/10/2013\r\n" +
                "\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Titel: C# Programmeren\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 21/10/2013\r\n" +
                "\r\n" +
                "Cursuscode: CNETIN\r\n" +
                "Titel: C# Programmeren\r\n" +
                "Duur: 5 dagen\r\n" +
                "Startdatum: 21/10/2013";

        assertThat(importer.getCoursesFromFile(file).size(), is(0));
    }



}