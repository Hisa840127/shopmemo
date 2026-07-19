package com.ishii.shopmemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ishii.shopmemo.model.Item;
import com.ishii.shopmemo.model.User;
import com.ishii.shopmemo.repository.ItemRepository;
import com.ishii.shopmemo.repository.UserRepository;

@Configuration
public class DataLoader {

	@Bean
	CommandLineRunner loadData(
	        ItemRepository itemRepository,
	        UserRepository userRepository,
	        PasswordEncoder passwordEncoder,
	        @Value("${app.ishii-password}") String ishiiPassword) {

	    return args -> {

	        User admin = userRepository.findByUsername("admin")
	                .orElseGet(() -> userRepository.save(
	                        new User(
	                                "admin",
	                                passwordEncoder.encode("1234"),
	                                "USER"
	                        )
	                ));

	       userRepository.findByUsername("ishii")
	        .orElseGet(() -> userRepository.save(
	                new User(
	                        "ishii",
	                        passwordEncoder.encode(ishiiPassword),
	                        "USER"
	                )
	        ));
	        
	        
	        if (itemRepository.count() == 0) {

	            Item egg = new Item(
	                    "卵", 10, "個",
	                    "ヨークフーズ",
	                    "残り少ない気がする");
	            egg.setUser(admin);

	            Item milk = new Item(
	                    "牛乳", 2, "本",
	                    "西友",
	                    "朝食用");
	            milk.setUser(admin);

	            itemRepository.save(egg);
	            itemRepository.save(milk);
	        }
	    };
	}

}
/*Dataloader.txt参照*/