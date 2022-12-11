package com.backend;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.entity.User;
import org.acme.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @Inject
    UserRepository userRepository;

    User user = new User();

    @Test
    @TestTransaction
    @Order(1)
    void testCreateUser(){
        user.setUsername("james");
        user.setPassword("james");
        given().contentType(ContentType.JSON).body(user).when().post("/authentication/signup");
        assert userRepository.find("username", user.getUsername()).firstResult().getUsername().equals(user.getUsername());
        assert userRepository.find("username", user.getUsername()).firstResult().getPassword().equals(user.getPassword());
    }

    @Test
    @TestTransaction
    @Order(2)
    void createDuplicateUser(){
        testCreateUser();
        assert given().contentType(ContentType.JSON).body(user).when().post("/authentication/signup").statusCode() == 403;
    }

    @Test
    @TestTransaction
    @Order(3)
    void loginUserCorrectDetails(){
        testCreateUser();
        assert given().contentType(ContentType.JSON).body(user).when().post("/authentication/login").statusCode() == 200;
    }

    @Test
    @TestTransaction
    @Order(4)
    void loginUserIncorrectDetails(){
        testCreateUser();
        user.setPassword("failed");
        assert given().contentType(ContentType.JSON).body(user).when().post("/authentication/login").statusCode() == 404;
    }

}
