package dev.lucasmattos.cooperative_vote.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;
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
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @OneToMany(mappedBy = "agenda")
    List<AgendaSession> agendaSessions = new LinkedList<>();

    @OneToMany(mappedBy = "agenda")
    List<AgendaVote> agendaVotes = new LinkedList<>();
}
