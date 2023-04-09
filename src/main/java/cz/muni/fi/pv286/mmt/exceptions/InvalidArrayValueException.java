package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

/**
 * An exception that is thrown when an invalid data format is found.
 */
public class InvalidArrayValueException extends IOException {
    public InvalidArrayValueException(String message) {
        super(message);
    }

    public InvalidArrayValueException() {
        super("Invalid format");
    }

}
