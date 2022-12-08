package org.acme.resources;
import antlr.ASTFactory;
import org.acme.entity.User;
import org.acme.repository.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/authentication")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    @POST
    @Path("/signup")
    @Transactional
    public Response createUser(User user){
        if (userRepository.find("username", user.getUsername()).firstResult()==null) {
            userRepository.persist(user);
            return Response.ok(user).build();
        }else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Path("/login")
    public Response loginUser(User user){
        User userrequest = userRepository.find("username", user.getUsername()).firstResult();
        if (userrequest == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else if (userrequest.getPassword().equals(user.getPassword())){
            return Response.ok(userrequest).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
