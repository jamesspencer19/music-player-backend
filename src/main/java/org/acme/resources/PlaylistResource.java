package org.acme.resources;

import org.acme.entity.Playlist;
import org.acme.entity.User;
import org.acme.repository.PlaylistRepository;

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

    @POST
    @Path("/addplaylist")
    @Transactional
    public Response addPlaylist(Playlist playlist){
        playlistRepository.persist(playlist);
        return Response.ok(playlist).build();
    }

    @GET
    @Path("/getplaylist/{username}")
    public Response getPlaylist(@PathParam("username") String username){
        if (playlistRepository.find("username", username).firstResult()!=null) {
            return Response.ok(playlistRepository.find("username", username).firstResult()).build();
        }
        else{
            return Response.ok().build();
        }
    }


}
