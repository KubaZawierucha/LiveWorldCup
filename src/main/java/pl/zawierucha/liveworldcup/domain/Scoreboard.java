package pl.zawierucha.liveworldcup.domain;

import java.util.List;

public interface Scoreboard {
    void startMatch(MatchParticipant home, MatchParticipant visitor);

    void increaseScore(MatchId matchId, ParticipantName participantName);

    void decreaseScore(MatchId matchId, ParticipantName participantName);

    void finishMatch(MatchId matchId);

    List<Match> getSummary();
}
