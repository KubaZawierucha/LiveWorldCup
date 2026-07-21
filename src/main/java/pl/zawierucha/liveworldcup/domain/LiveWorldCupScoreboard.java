package pl.zawierucha.liveworldcup.domain;

import pl.zawierucha.liveworldcup.domain.exceptions.NoActiveMatchException;
import pl.zawierucha.liveworldcup.domain.exceptions.ParticipantAlreadyInMatchException;
import pl.zawierucha.liveworldcup.domain.exceptions.ParticipantNotFoundInMatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LiveWorldCupScoreboard implements Scoreboard {

    public Map<MatchId, Match> matches = new HashMap<>();

    @Override
    public void startMatch(MatchParticipant home, MatchParticipant visitor) {
        Set<ParticipantName> currentlyPlayingTeams = matches.values().stream().flatMap(match -> Stream.of(match.getHome(), match.getVisitor())).collect(Collectors.toSet());

        if (currentlyPlayingTeams.contains(home.getName()) || currentlyPlayingTeams.contains(visitor.getName())) {
            throw new ParticipantAlreadyInMatchException();
        }

        MatchId newMatchId = MatchId.fromMatchParticipants(home, visitor);
        Match newMatch = new Match(home, visitor);
        matches.put(newMatchId, newMatch);
    }

    @Override
    public void increaseScore(MatchId matchId, ParticipantName participantName) {
        Match currentMatch = matches.get(matchId);
        if (currentMatch == null) {
            throw new NoActiveMatchException();
        }

        if (currentMatch.getHome().equals(participantName)) {
            currentMatch.home().increaseScore();
        } else if (currentMatch.getVisitor().equals(participantName)) {
            currentMatch.visitor().increaseScore();
        } else {
            throw new ParticipantNotFoundInMatchException();
        }
    }

    @Override
    public void decreaseScore(MatchId matchId, ParticipantName participantName) {
        Match currentMatch = matches.get(matchId);
        if (currentMatch == null) {
            throw new NoActiveMatchException();
        }

        if (currentMatch.getHome().equals(participantName)) {
            currentMatch.home().decreaseScore();
        } else if (currentMatch.getVisitor().equals(participantName)) {
            currentMatch.visitor().decreaseScore();
        } else {
            throw new ParticipantNotFoundInMatchException();
        }
    }

    @Override
    public void finishMatch(MatchId matchId) {
        if (!matches.containsKey(matchId)) {
            throw new NoActiveMatchException();
        }
        matches.remove(matchId);
    }

    // temp impl to check in tests
    @Override
    public List<Match> getSummary() {
        return matches.values().stream().toList();
    }


    // not used version of increase/decrease
    public void increaseScore(ParticipantName participant) {
        Optional<Match> optionalMatch = findMatchByParticipant(participant);
        optionalMatch.ifPresentOrElse(match -> match.getParticipantByName(participant).increaseScore(), () -> {
            throw new NoActiveMatchException();
        });
    }

    public void decreaseScore(ParticipantName participant) {
        Optional<Match> optionalMatch = findMatchByParticipant(participant);
        optionalMatch.ifPresentOrElse(match -> match.getParticipantByName(participant).decreaseScore(), () -> {
            throw new NoActiveMatchException();
        });
    }

    private Optional<Match> findMatchByParticipant(ParticipantName participant) {
        return matches.entrySet().stream().filter(entry -> entry.getKey().toString().contains(participant.name())).map(Map.Entry::getValue).findFirst();
    }
}
