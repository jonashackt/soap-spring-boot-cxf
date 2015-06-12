package de.codecentric.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("de.codecentric.soap")
@EnableAutoConfiguration
@EnableConfigurationProperties // needed, to automatically load rules.yml defined properties to Rule-Pojos fields, see http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
}