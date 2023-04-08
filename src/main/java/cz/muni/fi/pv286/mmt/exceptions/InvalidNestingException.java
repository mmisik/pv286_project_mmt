package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

public class InvalidNestingException extends IOException {
    public InvalidNestingException(String message) {
        super(message);
    }

    public InvalidNestingException() {
        super("Invalid nesting");
    }
}
