package com.backend.resources;

import com.backend.repository.PlaylistRepository;
import com.backend.entity.Playlist;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlist")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlaylistResource {
    @Inject
    PlaylistRepository playlistRepository;

    Playlist entity = new Playlist();

    @PATCH
    @Path("/editplaylist")
    @Transactional
    public Response addPlaylist(Playlist playlist){
        Playlist entity = playlistRepository.find("username", playlist.getUsername()).firstResult();
        if(entity!=null) {
            entity.songids = playlist.songids;
            playlistRepository.persist(entity);
            return Response.ok(playlistRepository.find("username", playlist.getUsername()).firstResult()).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/getplaylist/{username}")
    @Transactional
    public Response getPlaylist(@PathParam("username") String username){
        entity = playlistRepository.find("username", username).firstResult();
        if (entity==null) {
            Playlist playlist = new Playlist();
            playlist.setUsername(username);
            playlistRepository.persist(playlist);
            return Response.ok(playlistRepository.find("username", username).firstResult()).build();
        }
        return Response.ok(entity).build();
    }

}
