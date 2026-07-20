package pl.zawierucha.liveworldcup.domain;

import pl.zawierucha.liveworldcup.domain.exceptions.InvalidScoreException;

public class MatchParticipant {
    // should be final, once created, the name of the team should not be modified
    private final ParticipantName name;
    private int score;

    public MatchParticipant(ParticipantName name, int score) {
        this.name = name;
        this.score = score;
    }

    public MatchParticipant(ParticipantName name) {
        this(name, 0);
    }

    public ParticipantName getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore() {
        this.score++;
    }

    public void decreaseScore() {
        if (this.score <= 0) {
            throw new InvalidScoreException("Score cannot be negative.");
        }
        this.score--;
    }
}
