package com.lgcns.beinstagramclone;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.lgcns.beinstagramclone", "com.lgcns.beinstagramclone.user.auth"})
@SpringBootApplication
public class BeInstagramCloneApplication {

    public static void main(String[] args) {
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();
        env.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(BeInstagramCloneApplication.class, args);
    }
}   
