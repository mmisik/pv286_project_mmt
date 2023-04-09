package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

/**
 * This exception is used for invalid bit characters.
 */
public class InvalidBitCharacterException extends IOException {
    public InvalidBitCharacterException(String message) {
        super(message);
    }

    public InvalidBitCharacterException() {
        super("Invalid bit characters encountered.");
    }
}
