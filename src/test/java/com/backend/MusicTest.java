package com.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import com.backend.entity.Music;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MusicTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    Music music = new Music();

    //Add 10 songs to the music database and verify that all ten are retrieved
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

    //Retrieve a song using song ID assert that the values match
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

    //Retrieve song using invalid ID return Status 404 Not Found
    @Test
    @TestTransaction
    @Order(3)
    void testFailGetSong(){
        assert given().when().get("/music/song/" + "99").statusCode() == 404;
    }

}
