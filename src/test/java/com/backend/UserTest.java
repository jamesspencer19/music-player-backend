package com.backend;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import com.backend.entity.User;
import com.backend.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @Inject
    UserRepository userRepository;

    User user = new User();

    //Create a user and check that the credentials match those that have been saved to the Database
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

    //Test to create a user with a username that has already been used return Status 403
    @Test
    @TestTransaction
    @Order(2)
    void createDuplicateUser(){
        testCreateUser();
        System.out.println("Status Code: " + given().contentType(ContentType.JSON).body(user).when().post("/authentication/signup").statusCode());
        assert given().contentType(ContentType.JSON).body(user).when().post("/authentication/signup").statusCode() == 403;
    }

    //Login with correct details to return Status Code 200
    @Test
    @TestTransaction
    @Order(3)
    void loginUserCorrectDetails(){
        testCreateUser();
        assert given().contentType(ContentType.JSON).body(user).when().post("/authentication/login").statusCode() == 200;
    }

    //Login with incorrect details returns Status Code 404 Not Found
    @Test
    @TestTransaction
    @Order(4)
    void loginUserIncorrectDetails(){
        testCreateUser();
        user.setPassword("failed");
        assert given().contentType(ContentType.JSON).body(user).when().post("/authentication/login").statusCode() == 404;
    }

}
