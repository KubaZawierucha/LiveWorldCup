package pl.zawierucha.liveworldcup;

import java.util.List;

public interface Scoreboard {
    void startMatch(MatchParticipant home, MatchParticipant visitor);

    // do we need the ID, or can the match be identified by the participants?
    void updateScore(MatchParticipant home, MatchParticipant visitor, long matchId);

    // same here, how should we identify the match? maybe two methods?
    void finishMatch(long matchId);

    List<Match> getSummary();
}
