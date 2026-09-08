package org.fleetflow.souktransportbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SouktransportBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SouktransportBackendApplication.class, args);
    }

}
