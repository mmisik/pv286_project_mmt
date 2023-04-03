package cz.muni.fi.pv286.mmt.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * This class encapsulates all possible options.
 */
public class Options {
    private IoFormat inputFormat;
    private Optional<FromToOption> inputFromToOption = Optional.empty();
    private IoFormat outputFormat;
    private Optional<FromToOption> outputFromToOption = Optional.empty();
    private InputStream inputFile = System.in;
    private OutputStream outputFile = System.out;
    private char delimiter = '\n';
    private Optional<BracketType> bracketType = Optional.empty();
    private boolean wasFromOptionSet = false;
    private final boolean wasToOptionSet = false;
    private boolean wasBracketSet = false;

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
        if (inputFormat == IoFormat.Int && !wasFromOptionSet) {
            inputFromToOption = Optional.of(FromToOption.Big);
        }
        if (inputFormat == IoFormat.Bits && !wasFromOptionSet) {
            inputFromToOption = Optional.of(FromToOption.Left);
        }
        this.inputFormat = inputFormat;
    }


    /**
     * Get the input FromToOption.
     */
    public Optional<FromToOption> getInputFromToOption() {
        return inputFromToOption;
    }

    /**
     * Sets the input FromToOption.
     */
    public void setInputFromToOption(FromToOption inputFromToOption) {
        this.inputFromToOption = Optional.of(inputFromToOption);
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
        if (outputFormat == IoFormat.Int && !wasToOptionSet) {
            outputFromToOption = Optional.of(FromToOption.Big);
        }
        if (outputFormat == IoFormat.Array && !wasToOptionSet) {
            outputFromToOption = Optional.of(FromToOption.Hex);
        }
        if (outputFormat == IoFormat.Array && !wasBracketSet) {
            bracketType = Optional.of(BracketType.CurlyBracket);
        }

        this.outputFormat = outputFormat;
    }

    /**
     * Gets the output FromToOption.
     */
    public Optional<FromToOption> getOutputFromToOption() {
        return outputFromToOption;
    }

    /**
     * Sets the output FromToOption.
     */
    public void setOutputFromToOption(FromToOption outputFromToOption) {
        if (outputFromToOption == FromToOption.CurlyBracket) {
            setBracketOption(BracketType.CurlyBracket);
        } else if (outputFromToOption == FromToOption.SquareBracket) {
            setBracketOption(BracketType.SquareBracket);
        } else if (outputFromToOption == FromToOption.RegularBracket) {
            setBracketOption(BracketType.RegularBracket);
        } else {
            this.outputFromToOption = Optional.of(outputFromToOption);
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

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
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
}
