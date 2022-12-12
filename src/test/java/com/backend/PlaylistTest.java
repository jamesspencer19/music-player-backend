package com.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import com.backend.entity.Playlist;
import com.backend.repository.PlaylistRepository;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlaylistTest {

    @Inject
    PlaylistRepository playlistRepository;

    Playlist playlist = new Playlist();

    private ObjectMapper objectMapper = new ObjectMapper();

    //PrePopulate database with test playlist data
    @BeforeEach
    @Transactional
    void createplaylist(){
        playlist.setUsername("james");
        playlist.setSongids("1,2,3,4");
        playlistRepository.persist(playlist);
    }

    //Retrieve Blank Playlist as Playlist for this user hasn't yet been created
    @Test
    @Order(1)
    void getBlankPlaylist(){
        given().when().get("/playlist/getplaylist/" + "jamestest");
        System.out.println("Blank IDs: " + playlistRepository.find("username", "jamestest").firstResult().getSongids());
        assert playlistRepository.find("username", "jamestest").firstResult().getSongids() == null;
    }

    //Retrieve Pre-Populated Playlist using Username
    @Test
    @Order(2)
    void getPopulatedPlaylist() throws JsonProcessingException {
        String response = given().when().get("/playlist/getplaylist/" + "james").body().asString();
        Playlist responseplaylist = objectMapper.readValue(response,Playlist.class);
        System.out.println("Song IDs: " + responseplaylist.getSongids());
        assert responseplaylist.getSongids().equals(playlist.getSongids());
    }

    //Edit Playlist by removing the last song ID
    @Test
    @TestTransaction
    @Order(3)
    void patchPlaylist(){
        System.out.println("Previous IDs: " + playlist.getSongids());
        playlist.setSongids("1,2,3");
        given().contentType(ContentType.JSON).body(playlist).when().patch("/playlist/editplaylist/");
        System.out.println("New SongIds: "+playlist.getSongids());
        System.out.println("Patched Song IDs: " + playlistRepository.find("username", "james").firstResult().getSongids());
        assert playlistRepository.find("username", "james").firstResult().getSongids().equals("1,2,3");
    }

    //Return Error Status Code 404 when retrieving a playlist that doesn't exist
    @Test
    @TestTransaction
    @Order(4)
    void patchEmptyPlaylistError(){
        playlist.setUsername("jamestesting");
        System.out.println("Status Code: " + given().contentType(ContentType.JSON).body(playlist).when().patch("/playlist/editplaylist/").statusCode());
        assert given().contentType(ContentType.JSON).body(playlist).when().patch("/playlist/editplaylist/").statusCode() == 404;
    }

}