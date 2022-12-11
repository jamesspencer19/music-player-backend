package org.acme.resources;

import org.acme.entity.Music;
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

    @POST
    @Transactional
    @Path("/savesong")
    public Response saveSong(Music music){
        musicrepository.persist(music);
        return Response.ok().build();
    }

    @GET
    @Path("/library")
    public Response getLibrary(){
        return Response.ok(musicrepository.listAll()).build();
    }

    @GET
    @Path("/song/{id}")
    public Response getSongById(@PathParam("id") int id){
        Music music = musicrepository.find("id",id).firstResult();
        if(musicrepository.find("id",id).firstResult() != null){
            return Response.ok(musicrepository.find("id",id).firstResult()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
