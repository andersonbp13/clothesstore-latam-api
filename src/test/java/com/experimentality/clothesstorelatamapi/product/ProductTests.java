package com.experimentality.clothesstorelatamapi.product;

import com.experimentality.clothesstorelatamapi.BaseFT;
import com.experimentality.clothesstorelatamapi.Util.Enum.Country;
import com.experimentality.clothesstorelatamapi.product.dto.ProductDto;
import com.experimentality.clothesstorelatamapi.product.model.Product;
import com.experimentality.clothesstorelatamapi.product.repository.ProductRepository;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

public class ProductTests extends BaseFT {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void getAllTest() {

        List<Product> products = productRepository.findAll();

        List<?> response = RestAssured.given().urlEncodingEnabled(false)
                .get("/product")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .body()
                .as(List.class);

        Assert.assertEquals(products.size(), response.size());
    }

    @Test
    public void getTest() {

        Product product = productRepository.findById(1L).get();

        ProductDto productDto = RestAssured.given()
                .header("Content-Type", "application/json")
                .get("/product/find?id=1")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .body()
                .as(ProductDto.class);

        Assert.assertEquals(product.getName(), productDto.getName());
        Assert.assertEquals(product.getPrice(), productDto.getPrice());

    }

    @Test
    public void createTest() {

        ProductDto productDto = ProductDto.builder()
                .name("someName")
                .price(123456789D)
                .discount(31D)
                .section("someSection")
                .description("someDescription")
                .stock(23L)
                .country(Country.COLOMBIA)
                .build();

        Boolean response = RestAssured.given()
                .multiPart("frontImage", new File("src/test/resources/images/cat-gaad2bfa27_1920.jpg"))
                .multiPart("backImage", new File("src/test/resources/images/cat-gaad2bfa27_1920.jpg"))
                .multiPart("productDto", productDto, "application/json")
                .post("/product/create")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .body()
                .as(Boolean.class);

        Assert.assertEquals(true, response);
    }

}
