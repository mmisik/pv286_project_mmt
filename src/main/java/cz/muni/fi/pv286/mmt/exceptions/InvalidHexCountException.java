package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

public class InvalidHexCountException extends IOException {
    public InvalidHexCountException(String message) {
        super(message);
    }

    public InvalidHexCountException() {
        super("Input is not a multiple of 2.");
    }
}
