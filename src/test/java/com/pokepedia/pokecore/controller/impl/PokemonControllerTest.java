package com.pokepedia.pokecore.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokepedia.pokecore.controller.dto.request.PokemonRequest;
import com.pokepedia.pokecore.controller.dto.response.PokemonResponse;
import com.pokepedia.pokecore.controller.mapper.PokemonDtoMapper;
import com.pokepedia.pokecore.core.model.Pokemon;
import com.pokepedia.pokecore.core.service.PokemonService;
import com.pokepedia.pokecore.security.JwtAuthFilter;
import com.pokepedia.pokecore.security.JwtService;
import com.pokepedia.pokecore.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PokemonService pokemonService;

    @MockBean
    private PokemonDtoMapper mapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void findById_returns200() throws Exception {
        Pokemon pokemon = Pokemon.builder().id(1L).name("Pikachu").build();
        PokemonResponse response = new PokemonResponse(1L, 25, "Pikachu",
                List.of("Electric"), "Kanto", null);

        when(pokemonService.findById(1L)).thenReturn(pokemon);
        when(mapper.toResponse(pokemon)).thenReturn(response);

        mockMvc.perform(get("/v1/pokemon/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pikachu"));
    }

    @Test
    void create_withInvalidBody_returns400() throws Exception {
        PokemonRequest invalid = new PokemonRequest(null, "", List.of(), null);

        mockMvc.perform(post("/v1/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void findAll_returns200() throws Exception {
        Pokemon pokemon = Pokemon.builder().id(1L).name("Pikachu").build();
        PokemonResponse response = new PokemonResponse(1L, 25, "Pikachu",
                List.of("Electric"), "Kanto", null);
        org.springframework.data.domain.Page<Pokemon> page =
                new org.springframework.data.domain.PageImpl<>(List.of(pokemon));

        when(pokemonService.findAll(org.mockito.ArgumentMatchers.any())).thenReturn(page);
        when(mapper.toResponse(pokemon)).thenReturn(response);

        mockMvc.perform(get("/v1/pokemon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Pikachu"));
    }

    @Test
    void create_withValidBody_returns200() throws Exception {
        PokemonRequest request = new PokemonRequest(25, "Pikachu", List.of("Electric"), 1L);
        Pokemon pokemon = Pokemon.builder().id(1L).name("Pikachu").build();
        PokemonResponse response = new PokemonResponse(1L, 25, "Pikachu",
                List.of("Electric"), "Kanto", null);

        when(mapper.toDomain(request)).thenReturn(pokemon);
        when(pokemonService.create(pokemon)).thenReturn(pokemon);
        when(mapper.toResponse(pokemon)).thenReturn(response);

        mockMvc.perform(post("/v1/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pikachu"));
    }
}