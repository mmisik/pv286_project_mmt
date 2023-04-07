package cz.muni.fi.pv286.mmt.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * This class encapsulates all possible options.
 */
public class Options {
    private IoFormat inputFormat;
    private Optional<IoOption> inputFromToOption = Optional.empty();
    private IoFormat outputFormat;
    private Optional<IoOption> outputFromToOption = Optional.empty();
    private InputStream inputFile = System.in;
    private OutputStream outputFile = System.out;
    private String delimiter = "\n";
    private Optional<BracketType> bracketType = Optional.empty();
    private boolean wasFromOptionSet = false;
    private boolean wasToOptionSet = false;
    private boolean wasBracketSet = false;
    private boolean wasDelimiterSet = false;

    /**
     * Gets the input format.
     */
    public IoFormat getInputFormat() {
        return inputFormat;
    }

    /**
     * Sets the input format.
     */
    public void setInputFormat(IoFormat inputFormat) {
        if (inputFormat == IoFormat.INT && !wasFromOptionSet) {
            inputFromToOption = Optional.of(IoOption.BIG);
        }
        if (inputFormat == IoFormat.BITS && !wasFromOptionSet) {
            inputFromToOption = Optional.of(IoOption.LEFT);
        }
        this.inputFormat = inputFormat;
    }

    /**
     * Get the input FromToOption.
     */
    public Optional<IoOption> getInputFromToOption() {
        return inputFromToOption;
    }

    /**
     * Sets the input FromToOption.
     */
    public void setInputFromToOption(FromToOption inputFromToOption) {
        this.inputFromToOption = Optional.of(IoOption.from(inputFromToOption));
        wasFromOptionSet = true;
    }


    /**
     * Sets the bracket type option.
     */
    public void setBracketOption(BracketType bracketType) {
        this.bracketType = Optional.of(bracketType);
        wasBracketSet = true;
    }

    /**
     * Gets the output format.
     */
    public IoFormat getOutputFormat() {
        return outputFormat;
    }

    /**
     * Sets the output format.
     */
    public void setOutputFormat(IoFormat outputFormat) {
        if (outputFormat == IoFormat.INT && !wasToOptionSet) {
            outputFromToOption = Optional.of(IoOption.BIG);
        }
        if (outputFormat == IoFormat.ARRAY && !wasToOptionSet) {
            outputFromToOption = Optional.of(IoOption.HEX);
        }
        if (outputFormat == IoFormat.ARRAY && !wasBracketSet) {
            bracketType = Optional.of(BracketType.CURLY_BRACKET);
        }

        this.outputFormat = outputFormat;
    }

    /**
     * Gets the output FromToOption.
     */
    public Optional<IoOption> getOutputFromToOption() {
        return outputFromToOption;
    }

    /**
     * Sets the output FromToOption.
     */
    public void setOutputFromToOption(FromToOption outputFromToOption) {
        if (outputFromToOption == FromToOption.CURLY_BRACKET) {
            setBracketOption(BracketType.CURLY_BRACKET);
        } else if (outputFromToOption == FromToOption.SQUARE_BRACKET) {
            setBracketOption(BracketType.SQUARE_BRACKET);
        } else if (outputFromToOption == FromToOption.REGULAR_BRACKET) {
            setBracketOption(BracketType.REGULAR_BRACKET);
        } else {
            this.outputFromToOption = Optional.of(IoOption.from(outputFromToOption));
            wasToOptionSet = true;
        }
    }

    public InputStream getInputFile() {
        return inputFile;
    }

    public void setInputFile(InputStream inputFile) {
        this.inputFile = inputFile;
    }

    public OutputStream getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(OutputStream outputFile) {
        this.outputFile = outputFile;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        wasDelimiterSet = true;
    }

    public Optional<BracketType> getBracketType() {
        return bracketType;
    }

    private void setBracketType(Optional<BracketType> bracketType) {
        this.bracketType = bracketType;
    }

    private boolean wasFromOptionSet() {
        return wasFromOptionSet;
    }

    private boolean wasToOptionSet() {
        return wasToOptionSet;
    }

    private boolean wasBracketSet() {
        return wasBracketSet;
    }

    public boolean wasDelimiterSet() {
        return inputFormat != IoFormat.BYTES || wasDelimiterSet;
    }
}
