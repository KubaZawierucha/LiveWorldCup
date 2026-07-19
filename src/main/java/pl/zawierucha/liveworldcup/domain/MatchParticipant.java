package pl.zawierucha.liveworldcup.domain;

public class MatchParticipant {
    // should be final, once created, the name of the team should not be modified
    private final String name;
    private int score;

    public MatchParticipant(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public MatchParticipant(String name) {
        this(name, 0);
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
