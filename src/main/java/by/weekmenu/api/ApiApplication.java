package by.weekmenu.api;

import by.weekmenu.api.entity.Ownership;
import by.weekmenu.api.entity.OwnershipName;
import by.weekmenu.api.repository.OwnershipRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiApplication.class);
    }

    @Bean
    public CommandLineRunner initOwnership (OwnershipRepository ownershipRepository) {
        return (args) -> {
            if (ownershipRepository.findAll().spliterator().getExactSizeIfKnown()==0) {
                ownershipRepository.save(new Ownership(OwnershipName.ADMIN));
                ownershipRepository.save(new Ownership(OwnershipName.USER));
            }
        };
    }

}
