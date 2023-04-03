package cz.muni.fi.pv286.mmt.exceptions;

import java.io.IOException;

public class InvalidIntInputException extends IOException {
    public InvalidIntInputException(String message) {
        super(message);
    }

    public InvalidIntInputException() {
        super("Integer is not valid.");
    }
}
