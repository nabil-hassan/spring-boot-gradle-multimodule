package net.nh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplicationA {

    public static final Logger LOG = LoggerFactory.getLogger(ServiceApplicationA.class);
    public static void main(String[] args) {
        LOG.info("Starting service A");
        SpringApplication.run(ServiceApplicationA.class, args);
    }
}