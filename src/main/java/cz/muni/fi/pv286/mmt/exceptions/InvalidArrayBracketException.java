package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

/**
 * An exception that is thrown when an error related to brackets occurs.
 */
public class InvalidArrayBracketException extends IOException {
    public InvalidArrayBracketException(String message) {
        super(message);
    }

    public InvalidArrayBracketException() {
        super("Error with brackets occurred.");
    }
}
