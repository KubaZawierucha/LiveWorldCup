package pl.zawierucha.liveworldcup.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LiveWorldCupScoreboardTest {
    private final Scoreboard scoreboard = new LiveWorldCupScoreboard();

    @Test
    public void shouldAddToTheScoreboard_when_validMatchIsStarted() {
        // given:
        MatchParticipant home = new MatchParticipant("Mexico", 0);
        MatchParticipant visitor = new MatchParticipant("Canada", 0);

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
        MatchParticipant home = new MatchParticipant("Mexico", 0);
        MatchParticipant visitor = new MatchParticipant("Canada", 0);
        scoreboard.startMatch(home, visitor);

        // when:
        scoreboard.finishMatch(home, visitor);

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .noneMatch(match -> match.home().equals(home) && match.visitor().equals(visitor))
        );
    }

    @Test
    public void shouldUpdateScore_when_validMatchIsFound() {
        // given:
        MatchParticipant home = new MatchParticipant("Mexico", 0);
        MatchParticipant visitor = new MatchParticipant("Canada", 0);
        scoreboard.startMatch(home, visitor);
        home = new MatchParticipant("Mexico", 1);

        // when:
        scoreboard.updateScore(home, visitor);

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().getScore() == 1 && match.visitor().getScore() == 0)
        );
    }
}
