package com.experimentality.clothesstorelatamapi;

import com.squareup.okhttp.mockwebserver.MockWebServer;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestConfig.class})
public abstract class BaseFT {

    @LocalServerPort
    private int port;
    @Value("${external.api.port}")
    private int externalPort;

    private boolean firstTime = true;
    private static final String seedData = "database/data-ft.sql";

    @Autowired
    private DataSource dataSource;

    protected MockWebServer mockWebServer;

    @Before
    public void startUp() throws IOException {

        mockWebServer = new MockWebServer();
        mockWebServer.start(externalPort);

        resetData();

        RestAssured.port = this.port;

        if (firstTime) {
            firstTime = false;

            RestAssured.config = RestAssured.config()
                    .logConfig(new LogConfig().enableLoggingOfRequestAndResponseIfValidationFails());
        }

    }

    @After
    public void finish() throws IOException {
        mockWebServer.shutdown();
    }

    private void resetData() {
        try (Connection con = dataSource.getConnection()) {
            con.createStatement().executeQuery(getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error populating db: " + e);
        }
    }

    private String getQuery() {
        String sql = "";
        try (BufferedReader br = new BufferedReader(new FileReader(Thread.currentThread()
                .getContextClassLoader().getResource(seedData).getFile()))) {
            sql = br.lines()
                    .filter(line -> !line.startsWith("--"))
                    .collect(Collectors.joining());
        } catch (IOException ex) {
            throw new RuntimeException("Error parsing seed file");
        }

        return sql;
    }
}
