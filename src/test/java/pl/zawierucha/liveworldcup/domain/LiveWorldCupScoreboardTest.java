package pl.zawierucha.liveworldcup.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LiveWorldCupScoreboardTest {
    private final Scoreboard scoreboard = new LiveWorldCupScoreboard();

    @Test
    public void shouldAddToTheScoreboard_when_validMatchIsStarted() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));

        // when:
        scoreboard.startMatch(home, visitor);

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().equals(home) && match.visitor().equals(visitor))
        );
    }

    @Test
    public void shouldRemoveFromTheScoreboard_when_matchIsFinished() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        // when:
        scoreboard.finishMatch(matchId);

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .noneMatch(match -> match.home().equals(home) && match.visitor().equals(visitor))
        );
    }

    @Test
    public void shouldIncreaseScore_when_validMatchIsFound() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        // when:
        scoreboard.increaseScore(matchId, home.getName());

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().getScore() == 1 && match.visitor().getScore() == 0)
        );
    }

    @Test
    public void shouldDecreaseScore_when_validMatchIsFound() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);
        scoreboard.increaseScore(matchId, home.getName());

        // when:
        scoreboard.decreaseScore(matchId, home.getName());

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().getScore() == 0 && match.visitor().getScore() == 0)
        );
    }
}
