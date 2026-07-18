package pl.zawierucha.liveworldcup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LiveWorldCupScoreboard implements Scoreboard {

    // what id should I use here? maybe some teams-name-related key would be better?
    public Map<UUID, Match> matches = new HashMap<>();

    // decided to use domain-like objects instead of plain strings/integers - some kind of mapper before walking into
    // domain would be needed
    @Override
    public void startMatch(MatchParticipant home, MatchParticipant visitor) {
        // maybe the input should be validated before mapping it to the domain??
        // or at least can they be wrapped in validateInput-like methods
        if (home == null || visitor == null) {
            throw new IllegalArgumentException("Match participants must be present.");
        }

        if ((home.name() == null || home.name().isBlank()) || (visitor.name() == null || visitor.name().isBlank())) {
            throw new IllegalArgumentException("Match participants must have valid names.");
        }

        if (home.score() != 0 || visitor.score() != 0) {
            throw new IllegalArgumentException("Initial scores must be zero.");
        }

        Set<String> currentlyPlayingTeams = matches.values().stream().flatMap(match -> Stream.of(match.home().name(), match.visitor().name())).collect(Collectors.toSet());

        if (currentlyPlayingTeams.contains(home.name()) || currentlyPlayingTeams.contains(visitor.name())) {
            throw new IllegalArgumentException("One of the teams is already playing.");
        }

        // isn't UUID an overkill here?
        UUID newMatchId = UUID.randomUUID();
        Match newMatch = new Match(home, visitor, true);
        matches.put(newMatchId, newMatch);
    }

    @Override
    public void updateScore(MatchParticipant home, MatchParticipant visitor, long matchId) {
        // theoretically, the score always starts at zero, so update should be already > 0;
        // however we can simulate the VAR decision, where the first goal is canceled
        if (home.score() < 0 || visitor.score() < 0) {
            throw new IllegalArgumentException("Scores must be non-negative.");
        }

        Optional<UUID> matchIdOptional = getMatchByParticipants(home, visitor);
        if (matchIdOptional.isEmpty()) {
            throw new IllegalArgumentException("No active match found.");
        }

        // is a Record good choice? it is immutable, every time the match is updated, we create a new instance,
        // maybe an ordinary class would be better?
        matches.compute(matchIdOptional.get(), (key, match) -> new Match(home, visitor, true));
    }

    @Override
    public void finishMatch(MatchParticipant home, MatchParticipant visitor) {
        Optional<UUID> matchIdOptional = getMatchByParticipants(home, visitor);
        if (matchIdOptional.isEmpty()) {
            throw new IllegalArgumentException("No active match found.");
        }
        matches.remove(matchIdOptional.get());
    }

    // temp impl to check in tests
    @Override
    public List<Match> getSummary() {
        return matches.values().stream().toList();
    }

    // id should probably be changed to something like {homeName_visitorName} --> as the certain team can
    // play only one match in the same time, there won't be collisions in ID
    private Optional<UUID> getMatchByParticipants(MatchParticipant home, MatchParticipant visitor) {
        return matches.entrySet().stream().filter(entry -> {
            MatchParticipant matchHome = entry.getValue().home();
            MatchParticipant matchVisitor = entry.getValue().visitor();
            return matchHome.name().equals(home.name()) && matchVisitor.name().equals(visitor.name());
        }).map(Map.Entry::getKey).findFirst();
    }
}
