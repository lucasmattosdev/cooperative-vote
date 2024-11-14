package dev.lucasmattos.cooperative_vote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CooperativeVoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CooperativeVoteApplication.class, args);
    }
}
