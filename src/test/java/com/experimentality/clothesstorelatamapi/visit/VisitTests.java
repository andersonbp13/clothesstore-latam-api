package com.experimentality.clothesstorelatamapi.visit;

import com.experimentality.clothesstorelatamapi.BaseFT;
import com.experimentality.clothesstorelatamapi.visit.dto.MostWantedDto;
import com.experimentality.clothesstorelatamapi.visit.model.Visit;
import com.experimentality.clothesstorelatamapi.visit.repository.VisitRepository;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class VisitTests extends BaseFT {

    @Autowired
    private VisitRepository visitRepository;

    @Test
    public void mostWantedBadOffsetTest() {
        MostWantedDto response = RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/visit/most-wanted?offset=20&limit=10")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .body()
                .as(MostWantedDto.class);

        Assert.assertEquals(response.getProductDtos().size(), 0);
    }

    @Test
    public void mostWantedGoodOffsetTest() {
        MostWantedDto response = RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/visit/most-wanted?offset=0&limit=10")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .body()
                .as(MostWantedDto.class);

        Assert.assertEquals(response.getProductDtos().size(), 5);
    }

    @Test
    public void addVisitTest() {
        Optional<Visit> visitOptional = visitRepository.findByProductId(3L);
        Visit visit = visitOptional.get();
        long nroVisit = visit.getNroVisit();

        Assert.assertEquals(nroVisit, 17);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/visit/add-visit?productId=3")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200);

        visitOptional = visitRepository.findByProductId(3L);
        visit = visitOptional.get();
        nroVisit = visit.getNroVisit();

        Assert.assertEquals(nroVisit, 18);
    }
}
