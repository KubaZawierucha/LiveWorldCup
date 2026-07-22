package pl.zawierucha.liveworldcup.domain;

import org.junit.jupiter.api.Test;
import pl.zawierucha.liveworldcup.domain.exceptions.InvalidScoreException;
import pl.zawierucha.liveworldcup.domain.exceptions.NoActiveMatchException;
import pl.zawierucha.liveworldcup.domain.exceptions.ParticipantAlreadyInMatchException;
import pl.zawierucha.liveworldcup.domain.exceptions.ParticipantNotFoundInMatchException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LiveWorldCupScoreboardTest {
    private final Scoreboard scoreboard = new LiveWorldCupScoreboard();

    @Test
    public void shouldAddMatchToTheScoreboard_when_startedWithValidTeams() {
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
    public void shouldNotAddMatchToTheScoreboard_when_firstTeamAlreadyInMatch() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);

        MatchParticipant home2 = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor2 = new MatchParticipant(new ParticipantName("Honduras"));

        // then:
        assertThrows(ParticipantAlreadyInMatchException.class, () -> scoreboard.startMatch(home2, visitor2));

        assertTrue(scoreboard.getSummary().stream()
                .noneMatch(match -> match.home().equals(home2) && match.visitor().equals(visitor2))
        );
    }

    @Test
    public void shouldNotAddMatchToTheScoreboard_when_secondTeamAlreadyInMatch() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);

        MatchParticipant home2 = new MatchParticipant(new ParticipantName("Honduras"));
        MatchParticipant visitor2 = new MatchParticipant(new ParticipantName("Canada"));

        // then:
        assertThrows(ParticipantAlreadyInMatchException.class, () -> scoreboard.startMatch(home2, visitor2));

        assertTrue(scoreboard.getSummary().stream()
                .noneMatch(match -> match.home().equals(home2) && match.visitor().equals(visitor2))
        );
    }

    @Test
    public void shouldAllowStartingAnotherMatch_when_noParticipantIsPlaying() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);

        MatchParticipant home2 = new MatchParticipant(new ParticipantName("Spain"));
        MatchParticipant visitor2 = new MatchParticipant(new ParticipantName("Brazil"));

        // when:
        scoreboard.startMatch(home2, visitor2);

        // then:
        assertEquals(2, scoreboard.getSummary().size());
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().equals(home2) && match.visitor().equals(visitor2))
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
    public void shouldThrowException_when_finishingNonExistingMatch() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        // then:
        assertThrows(NoActiveMatchException.class, () -> scoreboard.finishMatch(matchId));
    }

    @Test
    public void shouldIncreaseHomeScore_when_validMatchAndHomeParticipantAreFound() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        // when:
        scoreboard.increaseScore(matchId, new ParticipantName("Mexico"));

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().getScore() == 1 && match.visitor().getScore() == 0)
        );
    }

    @Test
    public void shouldIncreaseVisitorScore_when_validMatchAndVisitorParticipantAreFound() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        // when:
        scoreboard.increaseScore(matchId, new ParticipantName("Canada"));

        // then:
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().getScore() == 0 && match.visitor().getScore() == 1)
        );
    }

    @Test
    public void shouldThrowException_when_increasingScoreForNonExistingMatch() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        // then:
        assertThrows(NoActiveMatchException.class, () -> scoreboard.increaseScore(matchId, new ParticipantName("Mexico")));
    }

    @Test
    public void shouldThrowException_when_increasingScoreForParticipantOutsideMatch() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);
        ParticipantName participantOutsideMatch = new ParticipantName("Spain");

        // then:
        assertThrows(ParticipantNotFoundInMatchException.class, () -> scoreboard.increaseScore(matchId, participantOutsideMatch));
    }

    @Test
    public void shouldDecreaseHomeScore_when_validMatchAndHomeParticipantAreFound() {
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

    @Test
    public void shouldThrowException_when_decreasingScoreForNonExistingMatch() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        // then:
        assertThrows(NoActiveMatchException.class, () -> scoreboard.decreaseScore(matchId, home.getName()));
    }

    @Test
    public void shouldThrowException_when_decreasingScoreForParticipantOutsideMatch() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);
        ParticipantName participantOutsideMatch = new ParticipantName("Spain");

        // then:
        assertThrows(ParticipantNotFoundInMatchException.class, () -> scoreboard.decreaseScore(matchId, participantOutsideMatch));
    }

    @Test
    public void shouldThrowException_when_decreasingScoreBelowZero() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        // then:
        assertThrows(InvalidScoreException.class, () -> scoreboard.decreaseScore(matchId, home.getName()));
    }

    @Test
    public void shouldReturnEmptyList_when_summaryRequestedForEmptyScoreboard() {
        // then:
        assertTrue(scoreboard.getSummary().isEmpty());
    }

    @Test
    public void shouldSortByTotalScoreThenByStartOrder_when_summaryRequested() {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);
        for (int i = 0; i < 5; i++) {
            scoreboard.increaseScore(matchId, visitor.getName());
        }

        home = new MatchParticipant(new ParticipantName("Spain"));
        visitor = new MatchParticipant(new ParticipantName("Brazil"));
        scoreboard.startMatch(home, visitor);
        matchId = MatchId.fromMatchParticipants(home, visitor);
        for (int i = 0; i < 10; i++) {
            scoreboard.increaseScore(matchId, home.getName());
        }
        for (int i = 0; i < 2; i++) {
            scoreboard.increaseScore(matchId, visitor.getName());
        }

        home = new MatchParticipant(new ParticipantName("Germany"));
        visitor = new MatchParticipant(new ParticipantName("France"));
        scoreboard.startMatch(home, visitor);
        matchId = MatchId.fromMatchParticipants(home, visitor);
        for (int i = 0; i < 2; i++) {
            scoreboard.increaseScore(matchId, home.getName());

            scoreboard.increaseScore(matchId, visitor.getName());
        }

        home = new MatchParticipant(new ParticipantName("Uruguay"));
        visitor = new MatchParticipant(new ParticipantName("Italy"));
        scoreboard.startMatch(home, visitor);
        matchId = MatchId.fromMatchParticipants(home, visitor);
        for (int i = 0; i < 6; i++) {
            scoreboard.increaseScore(matchId, home.getName());
            scoreboard.increaseScore(matchId, visitor.getName());
        }

        home = new MatchParticipant(new ParticipantName("Argentina"));
        visitor = new MatchParticipant(new ParticipantName("Australia"));
        scoreboard.startMatch(home, visitor);
        matchId = MatchId.fromMatchParticipants(home, visitor);
        for (int i = 0; i < 3; i++) {
            scoreboard.increaseScore(matchId, home.getName());
        }
        for (int i = 0; i < 1; i++) {
            scoreboard.increaseScore(matchId, visitor.getName());
        }

        List<MatchInfo> expected = List.of(
                new MatchInfo("Uruguay", "Italy", 6, 6, 3),
                new MatchInfo("Spain", "Brazil", 10, 2, 1),
                new MatchInfo("Mexico", "Canada", 0, 5, 0),
                new MatchInfo("Argentina", "Australia", 3, 1, 4),
                new MatchInfo("Germany", "France", 2, 2, 2)
        );

        // when:
        var summary = scoreboard.getSummary();

        // then:
        List<MatchInfo> actual = summary.stream()
                .map(MatchInfo::from)
                .toList();

        assertIterableEquals(expected, actual);
    }

    @Test
    public void shouldNotLoseScoreUpdates_when_manyThreadsIncreaseConcurrently() throws InterruptedException {
        // given:
        MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
        MatchParticipant visitor = new MatchParticipant(new ParticipantName("Canada"));
        scoreboard.startMatch(home, visitor);
        MatchId matchId = MatchId.fromMatchParticipants(home, visitor);

        int threads = 16;
        int incrementsPerThread = 1_000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        // when:
        for (int t = 0; t < threads; t++) {
            executor.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < incrementsPerThread; i++) {
                        scoreboard.increaseScore(matchId, home.getName());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS), "Concurrent increments did not finish in time");
        executor.shutdownNow();

        // then:
        int expectedScore = threads * incrementsPerThread;
        assertTrue(scoreboard.getSummary().stream()
                .anyMatch(match -> match.home().getScore() == expectedScore)
        );
    }

    @Test
    public void shouldStartExactlyOneMatch_when_manyThreadsStartOverlappingParticipantsConcurrently() throws InterruptedException {
        // given:
        int threads = 16;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicInteger successes = new AtomicInteger();
        AtomicInteger rejections = new AtomicInteger();

        // when: every thread tries to start a match sharing the "Mexico" participant
        for (int t = 0; t < threads; t++) {
            int index = t;
            executor.submit(() -> {
                try {
                    start.await();
                    MatchParticipant home = new MatchParticipant(new ParticipantName("Mexico"));
                    MatchParticipant visitor = new MatchParticipant(new ParticipantName("Opponent" + index));
                    scoreboard.startMatch(home, visitor);
                    successes.incrementAndGet();
                } catch (ParticipantAlreadyInMatchException e) {
                    rejections.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS), "Concurrent starts did not finish in time");
        executor.shutdownNow();

        // then: the check-then-act invariant held under contention
        assertEquals(1, successes.get());
        assertEquals(threads - 1, rejections.get());
        assertEquals(1, scoreboard.getSummary().size());
    }

    private record MatchInfo(String home, String visitor, int homeScore, int visitorScore, long order) {
        static MatchInfo from(Match match) {
            return new MatchInfo(
                    match.home().getName().name(),
                    match.visitor().getName().name(),
                    match.home().getScore(),
                    match.visitor().getScore(),
                    match.order()
            );
        }
    }
}
