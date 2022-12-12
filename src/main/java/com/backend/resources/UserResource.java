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

    //Create a user and save credentials to database
    @POST
    @Path("/signup")
    @Transactional
    public Response createUser(User user){
        //if the username does not already exist save user
        if (userRepository.find("username", user.getUsername()).firstResult()==null) {
            userRepository.persist(user);
            return Response.ok(user).build();
        }
        //return 403 if username already exists
        else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    //Verify credentials are correct to Login user
    @POST
    @Path("/login")
    public Response loginUser(User user){
        entity = userRepository.find("username", user.getUsername()).firstResult();
        //if credentials match return 200
        if (entity.getPassword().equals(user.getPassword())){
            return Response.ok(entity).build();
        }
        //return 404 if details do not match
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
