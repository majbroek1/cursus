package cursus.jaxrs;

import cursus.controller.Controller;
import cursus.domain.Registration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.sql.SQLException;

/**
 * Created by maart on 12-10-2016.
 */
@Path("/registrations")
public class JaxrsRegistration {

    Controller controller = new Controller();

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response addRegistration(Registration registration){
        try{
            if (controller.addRegistration(registration)){
                return Response.status(200).build();
            }
            else{
                return Response.status(400).build();
            }
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }


}
