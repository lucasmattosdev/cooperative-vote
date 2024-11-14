package dev.lucasmattos.cooperative_vote.core.domain.external;

public record User(UserStatus status) {
    public enum UserStatus {
        ABLE_TO_VOTE,
        UNABLE_TO_VOTE
    }
}
