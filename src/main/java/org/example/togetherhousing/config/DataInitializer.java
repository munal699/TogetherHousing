package org.example.togetherhousing.config;

import org.example.togetherhousing.model.UserTbl;
import org.example.togetherhousing.repository.userRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class DataInitializer implements CommandLineRunner {

    private final userRepository uRepo;

    public DataInitializer(userRepository uRepo) {
        this.uRepo = uRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed default Admin user if not exists
        if (!uRepo.existsByEmail("admin@togetherhousing.com")) {
            UserTbl admin = new UserTbl();
            admin.setFullname("System Administrator");
            admin.setEmail("admin@togetherhousing.com");
            admin.setPhone("+977 9800000000");
            admin.setAddress("Kathmandu, Nepal");
            admin.setPassword(DigestUtils.md5DigestAsHex("admin123".getBytes()));
            admin.setRole("ADMIN");

            uRepo.save(admin);
            System.out.println("Default Admin account seeded: admin@togetherhousing.com / admin123");
        }
    }
}
