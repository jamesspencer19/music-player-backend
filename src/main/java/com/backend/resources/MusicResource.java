package com.backend.resources;

import com.backend.entity.Music;
import com.backend.repository.MusicRepository;

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

    Music entity = new Music();

    //Endpoint used to save a song to the Database
    @POST
    @Transactional
    @Path("/savesong")
    public Response saveSong(Music music){
        musicrepository.persist(music);
        return Response.ok().build();
    }

    //Method to retrieve all songs from Database
    @GET
    @Path("/library")
    public Response getLibrary(){
        return Response.ok(musicrepository.listAll()).build();
    }

    //Method to retrieve song by ID from database
    @GET
    @Path("/song/{id}")
    public Response getSongById(@PathParam("id") int id){
        entity = musicrepository.find("id",id).firstResult();
        if(musicrepository.find("id",id).firstResult() != null){
            return Response.ok(musicrepository.find("id",id).firstResult()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
