package cursus.jaxrs;

import cursus.controller.Controller;
import cursus.domain.Course;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by maart on 12-10-2016.
 */
@Path("/courses")
public class JaxrsCourse {

    Controller controller = new Controller();

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/weeknr/{nr}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArrayList<Course> getCourseById(@PathParam("nr") String nr) throws SQLException {
        return controller.getCoursesFromWeek(nr);
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addCourses(String text) {
        try {
            if (controller.addCourses(text)) {
                return Response.status(200).build();
            } else {
                return Response.status(400).build();
            }
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }


}
