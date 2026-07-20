package pl.zawierucha.liveworldcup.domain.exceptions;

public class InvalidScoreException extends RuntimeException {
    public InvalidScoreException(String message) {
        super(message);
    }
}
