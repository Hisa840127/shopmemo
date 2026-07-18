package com.ishii.shopmemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ishii.shopmemo.repository.UserRepository;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**","/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                    .frameOptions(frame -> frame.disable())
                )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/list", true)
                .permitAll()
            )
            .logout(Customizer.withDefaults());

        return http.build();
    }
    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {

    	return username -> {

    		com.ishii.shopmemo.model.User user =
    				userRepository.findByUsername(username)
    						.orElseThrow(() ->
    								new UsernameNotFoundException(
    										"ユーザーが見つかりません: " + username));

    		return org.springframework.security.core.userdetails.User
    				.withUsername(user.getUsername())
    				.password(user.getPassword())
    				.roles(user.getRole())
    				.build();
    	};
    }
    

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}