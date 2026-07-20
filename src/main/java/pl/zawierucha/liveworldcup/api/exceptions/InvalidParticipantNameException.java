package pl.zawierucha.liveworldcup.api.exceptions;

public class InvalidParticipantNameException extends RuntimeException {
    public InvalidParticipantNameException(String message) {
        super(message);
    }
}
