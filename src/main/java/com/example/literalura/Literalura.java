package com.example.literalura;

import com.example.literalura.principal.Principal;
import com.example.literalura.repository.AutorRepository;
import com.example.literalura.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Literalura implements CommandLineRunner {

    @Autowired
    private LibrosRepository librosRepository;
    @Autowired
    private AutorRepository autorRepository;

    public static void main(String[] args) {
        SpringApplication.run(Literalura.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(librosRepository, autorRepository);
        principal.mostrarMenu();
    }
}