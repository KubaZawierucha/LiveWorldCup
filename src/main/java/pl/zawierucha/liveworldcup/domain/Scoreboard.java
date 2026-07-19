package pl.zawierucha.liveworldcup.domain;

import java.util.List;

public interface Scoreboard {
    void startMatch(MatchParticipant home, MatchParticipant visitor);

    // the matches score is usually increased (or decreased) by one, for only one participant
    // maybe it will be better to create two methods - increase score + decrease score?
    void updateScore(MatchParticipant home, MatchParticipant visitor);

    void finishMatch(MatchParticipant home, MatchParticipant visitor);

    List<Match> getSummary();
}
