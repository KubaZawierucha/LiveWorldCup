package pl.zawierucha.liveworldcup.domain;

import pl.zawierucha.liveworldcup.domain.exceptions.ParticipantNotFoundInMatchException;

public record Match(MatchParticipant home, MatchParticipant visitor) {
    public ParticipantName getHome() {
        return home.getName();
    }

    public ParticipantName getVisitor() {
        return visitor.getName();
    }

    public MatchParticipant getParticipantByName(ParticipantName name) {
        if (home.getName().equals(name)) {
            return home;
        } else if (visitor.getName().equals(name)) {
            return visitor;
        } else {
            throw new ParticipantNotFoundInMatchException();
        }
    }
}
