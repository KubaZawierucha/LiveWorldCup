package pl.zawierucha.liveworldcup.api;

import pl.zawierucha.liveworldcup.domain.MatchParticipant;
import pl.zawierucha.liveworldcup.domain.Scoreboard;

public class ScoreboardController {
    private final Scoreboard scoreboard;

    public ScoreboardController(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    // add the validation here
    public void startMatch(String homeName, String visitorName) {
        if (homeName == null || visitorName == null) {
            throw new IllegalArgumentException("Match participants must be present.");
        }
        if (homeName.isBlank() || visitorName.isBlank()) {
            throw new IllegalArgumentException("Match participants must have valid names.");
        }

        MatchParticipant home = new MatchParticipant(homeName);
        MatchParticipant visitor = new MatchParticipant(visitorName);

        scoreboard.startMatch(home, visitor);
    }

    // add the validation here
    public void updateScore(String homeName, int homeScore, String visitorName, int visitorScore) {
        MatchParticipant home = new MatchParticipant(homeName, homeScore);
        MatchParticipant visitor = new MatchParticipant(visitorName, visitorScore);

        scoreboard.updateScore(home, visitor);
    }

    // add the validation here
    public void finishMatch(String homeName, String visitorName) {
        MatchParticipant home = new MatchParticipant(homeName);
        MatchParticipant visitor = new MatchParticipant(visitorName);

        scoreboard.finishMatch(home, visitor);
    }

    public void getSummary() {
        scoreboard.getSummary();
    }
}
