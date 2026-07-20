package pl.zawierucha.liveworldcup.domain.exceptions;

public class NoActiveMatchException extends RuntimeException {
    public NoActiveMatchException() {
        super("No active match found");
    }
}
