package pl.zawierucha.liveworldcup;

public record MatchParticipant(
        String name,
        int score
) {
    public MatchParticipant(String name) {
        this(name, 0);
    }
}
