package pl.zawierucha.liveworldcup.domain;

public record MatchId(String id) {
    public static MatchId fromMatchParticipants(MatchParticipant home, MatchParticipant visitor) {
        return new MatchId(home.getName() + "_" + visitor.getName());
    }
}
