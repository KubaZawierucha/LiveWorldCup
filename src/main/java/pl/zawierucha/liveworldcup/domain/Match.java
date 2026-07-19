package pl.zawierucha.liveworldcup.domain;

public record Match(MatchParticipant home, MatchParticipant visitor) {
    public String getHomeName() {
        return home.getName();
    }

    public String getVisitorName() {
        return visitor.getName();
    }
}
