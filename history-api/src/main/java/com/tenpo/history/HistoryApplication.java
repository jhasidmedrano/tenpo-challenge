package com.tenpo.history;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class,
        FlywayAutoConfiguration.class
})
public class HistoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(HistoryApplication.class, args);
    }
}
