package cursus.jaxrs;

import cursus.dal.repositories.IRepository;
import cursus.dal.repositories.RepositoryOracle;
import cursus.domain.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by maart on 9-10-2016.
 */

@Path("/student")
public class JaxrsStudent {

    IRepository data = new RepositoryOracle();

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Student getStudent(@PathParam("id") String id){
         return null;
    }

    public ArrayList<String> getallTest(){
        return new ArrayList<>(Arrays.asList("aaa", "bbb"));
    }

}
