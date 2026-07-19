package pl.zawierucha.liveworldcup.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LiveWorldCupScoreboard implements Scoreboard {

    public Map<MatchId, Match> matches = new HashMap<>();

    // decided to use domain-like objects instead of plain strings/integers - some kind of mapper before walking into
    // domain would be needed
    @Override
    public void startMatch(MatchParticipant home, MatchParticipant visitor) {
        Set<String> currentlyPlayingTeams = matches.values()
                .stream()
                .flatMap(match -> Stream.of(match.getHomeName(), match.getVisitorName()))
                .collect(Collectors.toSet());

        if (currentlyPlayingTeams.contains(home.getName()) || currentlyPlayingTeams.contains(visitor.getName())) {
            throw new IllegalArgumentException("One of the teams is already playing.");
        }

        MatchId newMatchId = MatchId.fromMatchParticipants(home.getName(), visitor.getName());
        Match newMatch = new Match(home, visitor);
        matches.put(newMatchId, newMatch);
    }

    @Override
    public void updateScore(MatchParticipant home, MatchParticipant visitor) {
        // theoretically, the score always starts at zero, so update should be already > 0;
        // however we can simulate the VAR decision, where the first goal is canceled
        if (home.getScore() < 0 || visitor.getScore() < 0) {
            throw new IllegalArgumentException("Scores must be non-negative.");
        }

        MatchId matchId = MatchId.fromMatchParticipants(home.getName(), visitor.getName());
        matches.compute(matchId, (id, match) -> {
            if (match == null) {
                throw new IllegalArgumentException("No active match found.");
            }
            match.home().setScore(home.getScore());
            match.visitor().setScore(visitor.getScore());
            return match;
        });
    }

    @Override
    public void finishMatch(MatchParticipant home, MatchParticipant visitor) {
        MatchId matchId = MatchId.fromMatchParticipants(home.getName(), visitor.getName());
        if (!matches.containsKey(matchId)) {
            throw new IllegalArgumentException("No active match found.");
        }
        matches.remove(matchId);
    }

    // temp impl to check in tests
    @Override
    public List<Match> getSummary() {
        return matches.values().stream().toList();
    }
}
