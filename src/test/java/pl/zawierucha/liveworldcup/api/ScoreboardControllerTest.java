package pl.zawierucha.liveworldcup.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.zawierucha.liveworldcup.api.exceptions.InvalidParticipantNameException;
import pl.zawierucha.liveworldcup.domain.LiveWorldCupScoreboard;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ScoreboardControllerTest {

    private final LiveWorldCupScoreboard scoreboard = new LiveWorldCupScoreboard();
    private final ScoreboardController scoreboardController = new ScoreboardController(scoreboard);

    @Test
    public void shouldNotThrowException_when_bothTeamNamesAreValid_forStartMatch() {
        // given:
        String home = "Mexico";
        String visitor = "Canada";

        // then:
        Assertions.assertDoesNotThrow(() -> scoreboardController.startMatch(home, visitor));
    }

    @ParameterizedTest
    @MethodSource("invalidParticipants")
    public void shouldThrowException_when_participantNamesAreInvalid_forStartMatch(String home, String visitor) {
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.startMatch(home, visitor));
    }

    @Test
    public void shouldNotThrowException_when_matchAndScorerAreValid_forIncreaseScore() {
        // given:
        String home = "Mexico";
        String visitor = "Canada";
        scoreboardController.startMatch(home, visitor);

        // then:
        Assertions.assertDoesNotThrow(() -> scoreboardController.increaseScore(home, visitor, home));
    }

    @ParameterizedTest
    @MethodSource("invalidParticipants")
    public void shouldThrowException_when_participantNamesAreInvalid_forIncreaseScore(String home, String visitor) {
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.increaseScore(home, visitor, "Mexico"));
    }

    @Test
    public void shouldNotThrowException_when_matchAndScorerAreValid_forDecreaseScore() {
        // given:
        String home = "Mexico";
        String visitor = "Canada";
        scoreboardController.startMatch(home, visitor);
        scoreboardController.increaseScore(home, visitor, home);

        // then:
        Assertions.assertDoesNotThrow(() -> scoreboardController.decreaseScore(home, visitor, "Mexico"));
    }

    @ParameterizedTest
    @MethodSource("invalidParticipants")
    public void shouldThrowException_when_participantNamesAreInvalid_forDecreaseScore(String home, String visitor) {
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.decreaseScore(home, visitor, "Mexico"));
    }

    @Test
    public void shouldNotThrowException_when_validMatchIsFinished_forFinishMatch() {
        // given:
        String home = "Mexico";
        String visitor = "Canada";
        scoreboardController.startMatch(home, visitor);

        // then:
        Assertions.assertDoesNotThrow(() -> scoreboardController.finishMatch(home, visitor));
    }

    @ParameterizedTest
    @MethodSource("invalidParticipants")
    public void shouldThrowException_when_participantNamesAreInvalid_forFinishMatch(String home, String visitor) {
        Assertions.assertThrows(InvalidParticipantNameException.class, () -> scoreboardController.finishMatch(home, visitor));
    }

    private static Stream<Arguments> invalidParticipants() {
        return Stream.of(
                arguments(null, "Canada"),
                arguments("Mexico", null),
                arguments(" ", "Canada"),
                arguments("Mexico", " "),
                arguments("Mexico", "Mexico")
        );
    }
}
