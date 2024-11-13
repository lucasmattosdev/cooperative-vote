package dev.lucasmattos.cooperative_vote.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Entity
@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Associate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @NotNull
    String document;
}
