package com.enershare.user;

import com.enershare.user.model.Role;
import com.enershare.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role admin = new Role();
                admin.setName("ADMIN");
                roleRepository.save(admin);
            }
            if (roleRepository.findByName("USER").isEmpty()) {
                Role user = new Role();
                user.setName("USER");
                roleRepository.save(user);
            }
        };
    }
}
