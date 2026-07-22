package pl.zawierucha.liveworldcup.domain;

import pl.zawierucha.liveworldcup.domain.exceptions.NoActiveMatchException;
import pl.zawierucha.liveworldcup.domain.exceptions.ParticipantAlreadyInMatchException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO my own functionality
public class LiveWorldCupScoreboard implements Scoreboard {

    private final Map<MatchId, Match> matches = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private long matchOrder = 0L;

    @Override
    public void startMatch(MatchParticipant home, MatchParticipant visitor) {
        lock.lock();
        try {
            Set<ParticipantName> currentlyPlayingTeams = matches.values().stream().flatMap(match -> Stream.of(match.getHome(), match.getVisitor())).collect(Collectors.toSet());

            if (currentlyPlayingTeams.contains(home.getName()) || currentlyPlayingTeams.contains(visitor.getName())) {
                throw new ParticipantAlreadyInMatchException();
            }

            MatchId newMatchId = MatchId.fromMatchParticipants(home, visitor);
            Match newMatch = new Match(home, visitor, matchOrder++);
            matches.put(newMatchId, newMatch);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void increaseScore(MatchId matchId, ParticipantName participantName) {
        lock.lock();
        try {
            Match currentMatch = matches.get(matchId);
            if (currentMatch == null) {
                throw new NoActiveMatchException();
            }
            currentMatch.getParticipantByName(participantName).increaseScore();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void decreaseScore(MatchId matchId, ParticipantName participantName) {
        lock.lock();
        try {
            Match currentMatch = matches.get(matchId);
            if (currentMatch == null) {
                throw new NoActiveMatchException();
            }
            currentMatch.getParticipantByName(participantName).decreaseScore();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void finishMatch(MatchId matchId) {
        lock.lock();
        try {
            if (matches.remove(matchId) == null) {
                throw new NoActiveMatchException();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Match> getSummary() {
        lock.lock();
        try {
            return matches.values().stream().sorted(Comparator.comparing(Match::getTotalScore).thenComparing(Match::order).reversed()).toList();
        } finally {
            lock.unlock();
        }
    }
}
