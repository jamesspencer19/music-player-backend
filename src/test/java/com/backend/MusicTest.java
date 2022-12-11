package com.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.entity.Music;
import org.acme.entity.Playlist;
import org.acme.repository.MusicRepository;
import org.acme.resources.MusicResource;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MusicTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    Music music = new Music();

    @Test
    @TestTransaction
    @Order(1)
    void testGetLibrary(){
        for(int i =0;i<10;i++){
            music.setArtist("Artist"+i);
            music.setImagepath("ImagePath"+i);
            music.setSong("Song"+i);
            music.setSongpath("SongPath" +i);
            given().contentType(ContentType.JSON).body(music).when().post("/music/savesong");
        }
        Music[] musicList= given().when().get("/music/library").body().as(Music[].class);
        for (int i = 0; i <musicList.length;i++){
            assert musicList[i].getArtist().equals("Artist"+i);
            assert musicList[i].getImagepath().equals("ImagePath"+i);
            assert musicList[i].getSong().equals("Song"+i);
            assert musicList[i].getSongpath().equals("SongPath"+i);
        }
    }

    @Test
    @TestTransaction
    @Order(2)
    void testGetSong() throws JsonProcessingException {
        System.out.println(given().when().get("/music/song/" + "1").body().asString());
        Music musicResponse = objectMapper.readValue(given().when().get("/music/song/" + "1").body().asString(),Music.class);
        assert musicResponse.getArtist().equals("Artist0");
        assert musicResponse.getSongpath().equals("SongPath0");
        assert musicResponse.getSong().equals("Song0");
        assert musicResponse.getImagepath().equals("ImagePath0");
    }

    @Test
    @TestTransaction
    @Order(3)
    void testFailGetSong(){
        assert given().when().get("/music/song/" + "99").statusCode() == 404;
    }

}
