package pl.zawierucha.liveworldcup.domain.exceptions;

public class ParticipantNotFoundInMatchException extends RuntimeException {
    public ParticipantNotFoundInMatchException() {
        super("Participant not found in the match.");
    }
}
