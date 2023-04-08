package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

public class InvalidArrayArgumentException extends IOException {
    public InvalidArrayArgumentException(String message) {
        super(message);
    }
    public InvalidArrayArgumentException() {
        super("Invalid argument provided.");
    }
}
