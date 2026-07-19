package pl.zawierucha.liveworldcup.domain;

public record MatchId(String id) {
    public static MatchId fromMatchParticipants(String home, String visitor) {
        return new MatchId(home + "_" + visitor);
    }
}
