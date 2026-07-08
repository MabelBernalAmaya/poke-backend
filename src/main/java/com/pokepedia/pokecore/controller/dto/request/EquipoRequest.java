package com.pokepedia.pokecore.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EquipoRequest(
        @NotBlank(message = "El nombre del equipo es obligatorio")
        @Size(max = 25, message = "Maximo 25 caracteres")
        String nombre,

        @NotEmpty(message = "El equipo debe tener al menos un Pokemon")
        @Size(max = 6, message = "Maximo 6 Pokemon por equipo")
        List<Long> pokemonIds
) {}