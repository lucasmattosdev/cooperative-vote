package dev.lucasmattos.cooperative_vote.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AgendaSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @JsonIncludeProperties({"id"})
    @NotNull
    @ManyToOne
    Agenda agenda;

    @Enumerated(EnumType.STRING)
    @NotNull
    AgendaSessionStatus status;

    @JsonIgnore
    @NotNull
    @Version
    Long version;

    @NotNull
    Long durationInMinutes;

    @NotNull
    ZonedDateTime startAt;

    @NotNull
    ZonedDateTime endAt;
}
