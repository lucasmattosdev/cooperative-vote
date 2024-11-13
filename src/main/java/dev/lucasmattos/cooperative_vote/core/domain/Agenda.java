package dev.lucasmattos.cooperative_vote.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.LinkedList;
import java.util.List;
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
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @OneToOne
    AgendaSession lastAgendaSession;

    @Builder.Default
    @OneToMany(mappedBy = "agenda")
    List<AgendaSession> agendaSessions = new LinkedList<>();

    @Builder.Default
    @OneToMany(mappedBy = "agenda")
    List<AgendaVote> agendaVotes = new LinkedList<>();
}
