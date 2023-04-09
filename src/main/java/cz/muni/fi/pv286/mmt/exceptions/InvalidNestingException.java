package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

/**
 * An exception that is thrown when a bracket mismatch occurs.
 */
public class InvalidNestingException extends IOException {
    public InvalidNestingException(String message) {
        super(message);
    }

    public InvalidNestingException() {
        super("Invalid nesting");
    }
}
