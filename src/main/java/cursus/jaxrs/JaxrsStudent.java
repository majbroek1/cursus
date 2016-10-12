package cursus.jaxrs;

import cursus.controller.Controller;
import cursus.domain.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by maart on 9-10-2016.
 */

@Path("/students")
public class JaxrsStudent {

    Controller controller = new Controller();

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/id/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Student getStudentById(@PathParam("id") String id) throws SQLException {
        return controller.getStudentById(id);
    }

    @GET
    @Path("/{address}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArrayList<Student> getStudentsByEmail(@PathParam("address") String email) throws SQLException {
        return controller.getStudentsByEmail(email);
    }

    @GET
    @Path("/company/{id}")
    public ArrayList<Student> getStudentsByComapanyId(@PathParam("id") String id) throws SQLException {
        return controller.getStudentsByCompany(id);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addStudent(Student student){
        try{
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(Integer.toString(student.getId()));
            if (controller.addStudent(student)){
                return Response.created(uriBuilder.build()).build();
            }
            else{
                return Response.status(400).build();
            }
        }
        catch(Exception e){
            return Response.status(400).build();
        }
    }


}
