package org.acme.resources;

import org.acme.entity.User;
import org.acme.repository.MusicRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/music")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MusicResource {

    @Inject
    MusicRepository musicrepository;

    @GET
    @Path("/library")
    @Transactional
    public Response getLibrary(){
        return Response.ok(musicrepository.listAll()).build();
    }
}
