package com.experimentality.clothesstorelatamapi;

import com.experimentality.clothesstorelatamapi.external.firebase.service.FirebaseService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.experimentality")
public class TestConfig {

    @Bean
    @Primary
    public DataSource dataSource() {

        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:database/mysql-db-dialect.sql")
                .addScript("classpath:database/tables-db.sql")
                .generateUniqueName(true)
                .build();
    }

    @Bean
    public FirebaseService firebaseService() {
        return new FirebaseServiceMock();
    }

    @TestConfiguration
    public static class FirebaseServiceMock extends FirebaseService {

        public FirebaseServiceMock() {
            super("", "", 5000, "");
        }

        @Override
        public String uploadFile(MultipartFile multipartFile) {
            return "someImageName.jpg";
        }
    }
}
