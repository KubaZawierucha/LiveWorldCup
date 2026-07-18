package pl.zawierucha.liveworldcup;

import java.util.List;

public interface Scoreboard {
    void startMatch(MatchParticipant home, MatchParticipant visitor);

    // do we need the ID, or can the match be identified by the participants?
    void updateScore(MatchParticipant home, MatchParticipant visitor, long matchId);

    // the request won't have access to the internal ID, the match should be identified by the participants
    void finishMatch(MatchParticipant home, MatchParticipant visitor);

    List<Match> getSummary();
}
