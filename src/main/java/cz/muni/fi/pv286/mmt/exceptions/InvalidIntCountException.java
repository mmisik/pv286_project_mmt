package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

/**
 * This exception is used for invalid integer count.
 */
public class InvalidIntCountException extends IOException {
    public InvalidIntCountException(String message) {
        super(message);
    }

    public InvalidIntCountException() {
        super("Invalid number of bytes for an integer.");
    }
}
