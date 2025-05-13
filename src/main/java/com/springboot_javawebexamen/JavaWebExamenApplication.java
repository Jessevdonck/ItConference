package com.springboot_javawebexamen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import service.*;

@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
public class JavaWebExamenApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(JavaWebExamenApplication.class, args);
    }

    /* Service */
    @Bean
    EventService eventService() {
        return new EventServiceImpl();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new MyUserDetailsServiceImpl();
    }
}
