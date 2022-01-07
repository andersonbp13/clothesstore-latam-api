package com.experimentality.clothesstorelatamapi.firebase;

import com.experimentality.clothesstorelatamapi.BaseFT;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FirebaseTests extends BaseFT {

    @Test
    public void helloWorldTest() {
        String response = RestAssured.given()
                .header("Content-Type", "text/plain;charset=UTF-8")
                .get("/firebase/hello-world")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .asString();

        Assert.assertEquals("Hello World", response);
    }

    @Test
    public void uploadImageTest() {
        String response = RestAssured.given()
                .multiPart(new File("src/test/resources/images/cat-gaad2bfa27_1920.jpg"))
                .header("Content-Type", "multipart/form-data")
                .post("/firebase/image")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .asString();

        Assert.assertEquals("someImageName.jpg", response);
    }
}
