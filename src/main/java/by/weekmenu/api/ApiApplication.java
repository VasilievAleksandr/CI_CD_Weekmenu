package by.weekmenu.api;

import by.weekmenu.api.entity.MealType;
import by.weekmenu.api.entity.Ownership;
import by.weekmenu.api.entity.OwnershipName;
import by.weekmenu.api.entity.UnitOfMeasure;
import by.weekmenu.api.repository.MealTypeRepository;
import by.weekmenu.api.repository.OwnershipRepository;
import by.weekmenu.api.repository.UnitOfMeasureRepository;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
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
    public Module hibernateModule() {
        return new Hibernate5Module();
    }

    @Bean
    public CommandLineRunner initOwnershipAndBaseUOM(OwnershipRepository ownershipRepository,
                                                     UnitOfMeasureRepository unitOfMeasureRepository,
                                                     MealTypeRepository mealTypeRepository) {
        return (args) -> {
            if (ownershipRepository.findAll().spliterator().getExactSizeIfKnown() == 0) {
                ownershipRepository.save(new Ownership(OwnershipName.ADMIN));
                ownershipRepository.save(new Ownership(OwnershipName.USER));
            }
            if (!unitOfMeasureRepository.findByFullNameIgnoreCase("Грамм").isPresent()) {
                unitOfMeasureRepository.save(new UnitOfMeasure("гр", "Грамм"));
            }
            if (!unitOfMeasureRepository.findByFullNameIgnoreCase("Литр").isPresent()) {
                unitOfMeasureRepository.save(new UnitOfMeasure("л", "Литр"));
            }
            if (!unitOfMeasureRepository.findByFullNameIgnoreCase("Килограмм").isPresent()) {
                unitOfMeasureRepository.save(new UnitOfMeasure("кг", "Килограмм"));
            }

            if (mealTypeRepository.findAll().spliterator().getExactSizeIfKnown() == 0) {
                mealTypeRepository.save(new MealType("Завтрак", 10));
                mealTypeRepository.save(new MealType("Обед", 20));
                mealTypeRepository.save(new MealType("Ужин", 30));
            }
        };
    }

}
