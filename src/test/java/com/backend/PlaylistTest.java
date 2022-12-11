package com.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.entity.Playlist;
import org.acme.repository.PlaylistRepository;
import org.h2.util.json.JSONObject;
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

    @BeforeEach
    @Transactional
    void createplaylist(){
        playlist.setUsername("james");
        playlist.setSongids("1,2,3,4");
        playlistRepository.persist(playlist);
    }

    @Test
    @Order(1)
    void getBlankPlaylist(){
        given().when().get("/playlist/getplaylist/" + "jamestest");
        assert playlistRepository.find("username", "jamestest").firstResult().getUsername().equals("jamestest");
    }

    @Test
    @Order(2)
    void getPopulatedPlaylist() throws JsonProcessingException {
        String response = given().when().get("/playlist/getplaylist/" + "james").body().asString();
        Playlist responseplaylist = objectMapper.readValue(response,Playlist.class);
        assert responseplaylist.getSongids().equals(playlist.getSongids());
    }

    @Test
    @TestTransaction
    @Order(3)
    void patchPlaylist(){
        playlist.setSongids("1,2,3");
        given().contentType(ContentType.JSON).body(playlist).when().patch("/playlist/editplaylist/");
        assert playlistRepository.find("username", "james").firstResult().getSongids().equals("1,2,3");
    }


    @Test
    @TestTransaction
    @Order(4)
    void patchEmptyPlaylistError(){
        playlist.setUsername("jamestesting");
        assert given().contentType(ContentType.JSON).body(playlist).when().patch("/playlist/editplaylist/").statusCode() == 404;
    }

}
