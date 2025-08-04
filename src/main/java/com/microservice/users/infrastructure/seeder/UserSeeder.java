package com.microservice.users.infrastructure.seeder;

import com.microservice.users.domain.model.Users;
import com.microservice.users.domain.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void run(String... args) {
        System.out.println("🔥 Menjalankan UserSeeder...");

        if (usersRepository.count() == 0) {
            IntStream.range(1, 11).forEach(i -> {
                Users user = new Users();
                user.setName("User " + i);
                user.setEmail("user" + i + "@example.com");
                user.setPassword(bCryptPasswordEncoder.encode("password"  + i));
                user.setProvider("local");
                user.setProviderId("local-" + i);
                user.setAvatarUrl("https://example.com/avatar/" + i);
                user.setCreatedAt(Instant.now());
                user.setUpdatedAt(Instant.now());
                usersRepository.save(user);
            });
            System.out.println("✅ Seeder: 10 users berhasil dibuat");
        } else {
            System.out.println("⚠️ Data user sudah ada, seeding dilewati");
        }
    }

}
