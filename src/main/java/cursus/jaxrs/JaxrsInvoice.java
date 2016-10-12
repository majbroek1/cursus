package cursus.jaxrs;

import cursus.controller.Controller;
import cursus.domain.Invoice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by maart on 12-10-2016.
 */
@Path("/invoices")
public class JaxrsInvoice {

    Controller controller = new Controller();

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{nr}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArrayList<Invoice> getCourseById(@PathParam("nr") String nr) throws SQLException {
        return controller.getInvoicesForWeekNumber(nr);
    }
}
