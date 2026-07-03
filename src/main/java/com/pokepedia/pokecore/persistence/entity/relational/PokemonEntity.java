package com.pokepedia.pokecore.persistence.entity.relational;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "pokemon",
        indexes = { @Index(name = "idx_pokemon_number", columnList = "national_number")
        })
@Getter
// Lombok: genera SOLO getters para mantener inmutabilidad (Sección 10.2)
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA necesita constructor sin args
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // Solo Builder puede llamar al constructor completo
@Builder
// Patrón Builder para construcción segura
public class PokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_number", nullable = false, unique = true)
    private Integer nationalNumber;

    @Column(nullable = false, length = 100)
    private String name;

    // CORRECTO: FetchType.LAZY para colecciones — se carga solo si se pide explícitamente
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pokemon_type",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
     @Builder.Default
    private List<TypeEntity> types = new ArrayList<>();

    // CORRECTO: OneToOne con LAZY (evita carga innecesaria de stats)
    @OneToOne(mappedBy = "pokemon", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PokemonStatsEntity stats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}