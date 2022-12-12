package com.backend.resources;
import com.backend.entity.User;
import com.backend.repository.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authentication")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    User entity = new User();

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
        entity = userRepository.find("username", user.getUsername()).firstResult();
        if (entity.getPassword().equals(user.getPassword())){
            return Response.ok(entity).build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
