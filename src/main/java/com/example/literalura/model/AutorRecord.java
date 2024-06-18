package com.example.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AutorRecord(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") int fechaDeNacimiento,
        @JsonAlias("death_year") int fechaDeFallecimiento
) {
}