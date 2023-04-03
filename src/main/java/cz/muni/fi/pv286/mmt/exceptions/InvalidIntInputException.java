package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

/**
 * This exception is used for invalid integer input.
 */
public class InvalidIntInputException extends IOException {
    public InvalidIntInputException(String message) {
        super(message);
    }

    public InvalidIntInputException() {
        super("Integer is not valid.");
    }
}
