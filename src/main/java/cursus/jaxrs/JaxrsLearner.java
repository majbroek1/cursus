package cursus.jaxrs;

import cursus.dal.DataAccess;
import cursus.dal.oracle.DataAccessOracle;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by maart on 9-10-2016.
 */

@Path("/learners")
public class JaxrsLearner {

    DataAccess data = new DataAccessOracle();

    @Context
    UriInfo uriInfo;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArrayList<String> getallTest(){
        return new ArrayList<>(Arrays.asList("aaa", "bbb"));
    }

}
