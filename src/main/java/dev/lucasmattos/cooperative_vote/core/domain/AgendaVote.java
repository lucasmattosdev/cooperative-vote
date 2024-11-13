package dev.lucasmattos.cooperative_vote.core.domain;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class AgendaVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @JsonIncludeProperties({"id"})
    @NotNull
    @ManyToOne
    Agenda agenda;

    @NotNull
    @Enumerated(EnumType.STRING)
    AgendaVoteValue value;

    ZonedDateTime votedAt;

    @JsonIncludeProperties({"id", "document"})
    @NotNull
    @ManyToOne
    Associate associate;
}
