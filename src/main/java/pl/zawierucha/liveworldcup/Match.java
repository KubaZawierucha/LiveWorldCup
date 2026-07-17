package pl.zawierucha.liveworldcup;

public record Match(
        MatchParticipant home,
        MatchParticipant visitor,
        boolean isActive
) {
}
