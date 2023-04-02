package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

public class InvalidHexCharacterException extends IOException {
    public InvalidHexCharacterException(String message) {
        super(message);
    }

    public InvalidHexCharacterException() {
        super("Invalid hex characters encountered.");
    }
}
