package pl.zawierucha.liveworldcup.domain;

import pl.zawierucha.liveworldcup.domain.exceptions.NoActiveMatchException;
import pl.zawierucha.liveworldcup.domain.exceptions.ParticipantAlreadyInMatchException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO my own functionality
public class LiveWorldCupScoreboard implements Scoreboard {

    private final Map<MatchId, Match> matches = new HashMap<>();
    private long matchOrder = 0L;
    private final Object lock = new Object();

    @Override
    public void startMatch(MatchParticipant home, MatchParticipant visitor) {
        synchronized (lock) {
            Set<ParticipantName> currentlyPlayingTeams = matches.values().stream().flatMap(match -> Stream.of(match.getHome(), match.getVisitor())).collect(Collectors.toSet());

            if (currentlyPlayingTeams.contains(home.getName()) || currentlyPlayingTeams.contains(visitor.getName())) {
                throw new ParticipantAlreadyInMatchException();
            }

            MatchId newMatchId = MatchId.fromMatchParticipants(home, visitor);
            Match newMatch = new Match(home, visitor, matchOrder++);
            matches.put(newMatchId, newMatch);
        }
    }

    @Override
    public void increaseScore(MatchId matchId, ParticipantName participantName) {
        synchronized (lock) {
            Match currentMatch = matches.get(matchId);
            if (currentMatch == null) {
                throw new NoActiveMatchException();
            }
            currentMatch.getParticipantByName(participantName).increaseScore();
        }
    }

    @Override
    public void decreaseScore(MatchId matchId, ParticipantName participantName) {
        synchronized (lock) {
            Match currentMatch = matches.get(matchId);
            if (currentMatch == null) {
                throw new NoActiveMatchException();
            }
            currentMatch.getParticipantByName(participantName).decreaseScore();
        }
    }

    @Override
    public void finishMatch(MatchId matchId) {
        synchronized (lock) {
            if (matches.remove(matchId) == null) {
                throw new NoActiveMatchException();
            }
        }
    }

    @Override
    public List<Match> getSummary() {
        synchronized (lock) {
            return matches.values().stream().sorted(Comparator.comparing(Match::getTotalScore).thenComparing(Match::order).reversed()).toList();
        }
    }
}
