package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

/**
 * This exception is used for invalid bit input.
 */
public class InvalidBitInputException extends IOException {
    public InvalidBitInputException(String message) {
        super(message);
    }

    public InvalidBitInputException() {
        super("Input is not a valid bit string.");
    }
}
