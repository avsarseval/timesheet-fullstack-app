package com.aksigorta.timesheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TimesheetApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetApplication.class, args);
	}
    @Bean // Bu anotasyon, Spring'e bu metodun bir "Bean" (ortak nesne) ürettiğini söyler.
    public PasswordEncoder passwordEncoder() {
        // Projenin herhangi bir yerinde PasswordEncoder istendiğinde,
        // Spring bu metodu çalıştırıp bir BCryptPasswordEncoder nesnesi verecek.
        return new BCryptPasswordEncoder();
    }
}
