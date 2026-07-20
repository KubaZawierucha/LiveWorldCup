package pl.zawierucha.liveworldcup.api.exceptions;

public class ParticipantAlreadyInMatchException extends RuntimeException {
    public ParticipantAlreadyInMatchException() {
        super("One of the teams is already playing.");
    }
}
