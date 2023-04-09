package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

/**
 * An exception that is thrown when an invalid array argument is provided.
 */
public class InvalidArrayArgumentException extends IOException {
    public InvalidArrayArgumentException(String message) {
        super(message);
    }

    public InvalidArrayArgumentException() {
        super("Invalid argument provided.");
    }
}
