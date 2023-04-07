package cz.muni.fi.pv286.mmt.exceptions;

public class InvalidNestingException extends Exception {
    public InvalidNestingException(String message) {
        super(message);
    }

    public InvalidNestingException() {
        super("Invalid nesting");
    }
}
