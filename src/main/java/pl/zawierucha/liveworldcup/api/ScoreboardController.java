package pl.zawierucha.liveworldcup.api;

import pl.zawierucha.liveworldcup.api.exceptions.InvalidParticipantNameException;
import pl.zawierucha.liveworldcup.domain.MatchId;
import pl.zawierucha.liveworldcup.domain.MatchParticipant;
import pl.zawierucha.liveworldcup.domain.ParticipantName;
import pl.zawierucha.liveworldcup.domain.Scoreboard;

public class ScoreboardController {
    private final Scoreboard scoreboard;

    public ScoreboardController(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void startMatch(String homeName, String visitorName) {
        validateParticipants(homeName, visitorName);

        MatchParticipant home = new MatchParticipant(new ParticipantName(homeName));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName(visitorName));

        scoreboard.startMatch(home, visitor);
    }

    public void increaseScore(String homeName, String visitorName, String goalFor) {
        validateParticipants(homeName, visitorName);

        MatchParticipant home = new MatchParticipant(new ParticipantName(homeName));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName(visitorName));
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        scoreboard.increaseScore(matchId, new ParticipantName(goalFor));
    }

    public void decreaseScore(String homeName, String visitorName, String goalFor) {
        validateParticipants(homeName, visitorName);

        MatchParticipant home = new MatchParticipant(new ParticipantName(homeName));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName(visitorName));
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        scoreboard.decreaseScore(matchId, new ParticipantName(goalFor));
    }

    public void finishMatch(String homeName, String visitorName) {
        validateParticipants(homeName, visitorName);

        MatchParticipant home = new MatchParticipant(new ParticipantName(homeName));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName(visitorName));
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        scoreboard.finishMatch(matchId);
    }

    public void getSummary() {
        scoreboard.getSummary();
    }

    private void validateParticipants(String homeName, String visitorName) {
        if (homeName == null || visitorName == null) {
            throw new InvalidParticipantNameException("Match participants must be present.");
        }
        if (homeName.isBlank() || visitorName.isBlank()) {
            throw new InvalidParticipantNameException("Match participants must have valid names.");
        }
        if (homeName.equals(visitorName)) {
            throw new InvalidParticipantNameException("Match participants must have different names.");
        }
    }
}
