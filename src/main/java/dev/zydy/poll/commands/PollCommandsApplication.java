package dev.zydy.poll.commands;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
public class PollCommandsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PollCommandsApplication.class, args);
    }

}
